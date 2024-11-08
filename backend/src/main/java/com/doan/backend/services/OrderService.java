package com.doan.backend.services;

import com.doan.backend.dto.request.OrderRequest;
import com.doan.backend.dto.request.UpdateOrderRequest;
import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.OrderResponse;
import com.doan.backend.entity.*;
import com.doan.backend.enums.*;
import com.doan.backend.mapper.OrderMapper;
import com.doan.backend.repositories.*;
import com.doan.backend.utils.CodeUtils;
import com.doan.backend.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class OrderService {

    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    ProductInventoryRepository productInventoryRepository;
    OrderMapper orderMapper;
    DiscountRepository discountRepository;
    ShippingAddressRepository shippingAddressRepository;
    PromotionProductRepository promotionProductRepository;
    PromotionService promotionService;
    InvoiceRepository invoiceRepository;
    PaymentRepository paymentRepository;
    PaymentService paymentService;

    @Transactional
    public ApiResponse<OrderResponse> createOrderFromCart(OrderRequest orderRequest) {
        Cart cart = cartRepository.findByUserId(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        ShippingAddress shippingAddress = shippingAddressRepository.findById(orderRequest.getShippingAddressId())
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));

        Iterable<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPriceBeforeDiscount = BigDecimal.ZERO;
        BigDecimal totalPriceAfterDiscount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            ProductInventory productInventory = productInventoryRepository
                    .findByProductIdAndSizeId(cartItem.getProduct().getId(), cartItem.getSize().getId())
                    .orElseThrow(() -> new RuntimeException("Product inventory not found"));

            BigDecimal itemPrice = promotionService.applyPromotionToProduct(cartItem.getProduct());

            if (productInventory.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + cartItem.getProduct().getName());
            }

            Optional<Promotion> promotionOptional = promotionProductRepository.findActivePromotionByProductId(cartItem.getProduct().getId(), LocalDateTime.now());

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setSize(cartItem.getSize());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(itemPrice);
            orderItem.setOrder(null);

            orderItem.setPromotion(promotionOptional.orElse(null));

            orderItems.add(orderItem);

            totalPriceBeforeDiscount = totalPriceBeforeDiscount.add(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            totalPriceAfterDiscount = totalPriceAfterDiscount.add(itemPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            productInventory.setQuantity(productInventory.getQuantity() - cartItem.getQuantity());
            productInventoryRepository.save(productInventory);
        }

        if (orderRequest.getDiscountId() != null && !orderRequest.getDiscountId().isEmpty()) {
            Optional<Discount> discount = discountRepository.findById(orderRequest.getDiscountId());
            if (discount.isPresent() && discount.get().getMinOrderValue().compareTo(totalPriceAfterDiscount) <= 0) {
                BigDecimal discountValue = totalPriceAfterDiscount.multiply(discount.get().getDiscountPercentage().divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));

                if (discount.get().getDiscountType() == DiscountType.PERCENTAGE) {
                    if (discountValue.compareTo(discount.get().getMaxDiscountValue()) > 0) {
                        discountValue = discount.get().getMaxDiscountValue();
                    }
                    totalPriceAfterDiscount = totalPriceAfterDiscount.subtract(discountValue);
                } else {
                    totalPriceAfterDiscount = totalPriceAfterDiscount.subtract(discount.get().getDiscountValue());
                }
            }

            if (totalPriceAfterDiscount.compareTo(BigDecimal.ZERO) <= 0) {
                totalPriceAfterDiscount = BigDecimal.ZERO;
            }
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatusEnum.PENDING);
        order.setTotalPriceBeforeDiscount(totalPriceBeforeDiscount);
        order.setTotalPriceAfterDiscount(totalPriceAfterDiscount);
        order.setShippingAddress(shippingAddress);

        Order savedOrder = orderRepository.save(order);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }

        String invoiceNumber = CodeUtils.generateUniqueCode(Constants.INVOICE_PREFIX, invoiceRepository.count() + 4);

        Invoice invoice = new Invoice();
        invoice.setOrder(savedOrder);
        invoice.setTotalAmount(totalPriceAfterDiscount);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setStatus(InvoiceStatusEnum.UNPAID);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        if (totalPriceAfterDiscount.equals(BigDecimal.ZERO)) {
            invoice.setStatus(InvoiceStatusEnum.PAID);
            invoiceRepository.save(invoice);
        }

        Payment payment = new Payment();
        payment.setInvoice(savedInvoice);
        payment.setAmount(BigDecimal.ZERO);
        payment.setPaymentMethod(PaymentMethodEnum.TRANSFER);
        if (totalPriceAfterDiscount.equals(BigDecimal.ZERO)) {
            invoice.setStatus(InvoiceStatusEnum.PAID);
        } else {
            payment.setQrCodeUrl(paymentService.createPaymentLink(savedInvoice.getId()));
            payment.setPaymentStatus(PaymentStatusEnum.PENDING);
        }
        paymentRepository.save(payment);

        cartItemRepository.deleteAll(cartItems);

        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .message("Order created successfully")
                .result(orderMapper.toOrderResponse(savedOrder))
                .build();
    }

    @Transactional
    public ApiResponse<OrderResponse> clientEditOrder(UpdateOrderRequest clientUpdateOrderRequest) {
        Order order = orderRepository.findById(clientUpdateOrderRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(clientUpdateOrderRequest.getOrderId())) {
            throw new RuntimeException("You do not have permission to edit this order");
        }

        if (order.getStatus() != OrderStatusEnum.PENDING) {
            throw new RuntimeException("Order cannot be edited at this stage");
        }

        if (clientUpdateOrderRequest.getShippingAddressId() != null) {
            ShippingAddress shippingAddress = shippingAddressRepository.findById(clientUpdateOrderRequest.getShippingAddressId())
                    .orElseThrow(() -> new RuntimeException("Shipping address not found"));
            order.setShippingAddress(shippingAddress);
        }

        Order updatedOrder = orderRepository.save(order);

        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .message("Order updated successfully")
                .result(orderMapper.toOrderResponse(updatedOrder))
                .build();
    }

    @Transactional
    public ApiResponse<OrderResponse> adminEditOrder(String orderId, OrderRequest orderRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (orderRequest.getShippingAddressId() != null) {
            ShippingAddress shippingAddress = shippingAddressRepository.findById(orderRequest.getShippingAddressId())
                    .orElseThrow(() -> new RuntimeException("Shipping address not found"));
            order.setShippingAddress(shippingAddress);
        }

        if (orderRequest.getStatus() != null) {
            order.setStatus(orderRequest.getStatus());
        }

        Order updatedOrder = orderRepository.save(order);

        return ApiResponse.<OrderResponse>builder()
                .code(200)
                .message("Order updated successfully")
                .result(orderMapper.toOrderResponse(updatedOrder))
                .build();
    }

    public ApiResponse<Iterable<OrderResponse>> getOrderByUserId(String userId) {
        Iterable<Order> orders = orderRepository.findByUserId(userId);
        return ApiResponse.<Iterable<OrderResponse>>builder()
                .code(200)
                .message("Order retrieved successfully")
                .result(orderMapper.toOrderResponseIterable(orders))
                .build();
    }

    public ApiResponse<Page<OrderResponse>> getOrdersForAdmin(String productName, String customerName, OrderStatusEnum status, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findOrdersForAdmin(productName, customerName, status, pageable);
        Page<OrderResponse> responsePage = ordersPage.map(orderMapper::toOrderResponse);

        return ApiResponse.<Page<OrderResponse>>builder()
                .code(200)
                .message("Orders retrieved successfully")
                .result(responsePage)
                .build();
    }
}
