package com.doan.backend.services;

import com.doan.backend.dto.response.ApiResponse;
import com.doan.backend.dto.response.InvoiceResponse;
import com.doan.backend.entity.Invoice;
import com.doan.backend.enums.InvoiceStatusEnum;
import com.doan.backend.mapper.InvoiceMapper;
import com.doan.backend.repositories.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class InvoiceService {

    InvoiceRepository invoiceRepository;
    InvoiceMapper invoiceMapper;


    public ApiResponse<InvoiceResponse> getInvoiceByOrderId(String orderId) {
        Invoice invoice = invoiceRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("Invoice not found"));

        return ApiResponse.<InvoiceResponse>builder()
                .code(200)
                .message("Success")
                .result(invoiceMapper.toInvoiceResponse(invoice))
                .build();
    }

    public ApiResponse<InvoiceResponse> getInvoiceById(String invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("Invoice not found"));

        return ApiResponse.<InvoiceResponse>builder()
                .code(200)
                .message("Success")
                .result(invoiceMapper.toInvoiceResponse(invoice))
                .build();
    }

    public ApiResponse<Iterable<InvoiceResponse>> getAllInvoiceSearchName(String customerName, Pageable pageable) {
        return ApiResponse.<Iterable<InvoiceResponse>>builder()
                .code(200)
                .message("Success")
                .result(invoiceMapper.toInvoiceResponseIterable(invoiceRepository.findByAllSearchName(customerName, pageable)))
                .build();
    }

    public Invoice updateInvoiceStatus(Invoice invoice, InvoiceStatusEnum invoiceStatusEnum) {
        invoice.setStatus(invoiceStatusEnum);
        return invoiceRepository.save(invoice);
    }

}
