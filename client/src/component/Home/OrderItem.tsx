import { useState } from "react";
import { OrderItemResponse, OrderResponse } from "../../types/order";
import { OrderStatusEnum, PaymentStatusEnum } from "../../types/enums";
import { Badge, Button, Card, Col, Divider, Image, List, Row, Space, Tag, Typography } from "antd";
import { ClockCircleOutlined, CreditCardOutlined, ShoppingOutlined, TagOutlined } from "@ant-design/icons";
import "../../sass/order.scss";
import { formatDate } from "../../services/date";
import { formatPrice } from "../../services/format-price";
import { useGetInvoicesByOrderIdQuery } from "../../redux/api/invoice";
import { useNavigate } from "react-router-dom";
import { showCustomNotification } from "../../utils/notification";

const { Text } = Typography;

const OrderItem: React.FC<{ order: OrderResponse }> = ({ order }) => {
    const navigate = useNavigate();
    const [expanded, setExpanded] = useState(false);
    const { data: invoiceData } = useGetInvoicesByOrderIdQuery(order.id);

    const orderStatusColors = {
        [OrderStatusEnum.PENDING]: 'gold',
        [OrderStatusEnum.CONFIRMED]: 'blue',
        [OrderStatusEnum.SHIPPING]: 'cyan',
        [OrderStatusEnum.DELIVERED]: 'geekblue',
        [OrderStatusEnum.COMPLETED]: 'green',
        [OrderStatusEnum.CANCELLED]: 'red',
    };

    const paymentStatusColors = {
        [PaymentStatusEnum.PENDING]: 'gold',
        [PaymentStatusEnum.COMPLETED]: 'green',
        [PaymentStatusEnum.FAILED]: 'red',
        [PaymentStatusEnum.CANCELED]: 'red',
    };

    const getStatusColor = (status: OrderStatusEnum | PaymentStatusEnum) => {
        return orderStatusColors[status as OrderStatusEnum] || paymentStatusColors[status as PaymentStatusEnum];
    };

    const handlePayImmediately = () => {
        if (invoiceData?.result?.payment?.qrCodeUrl) {
            window.location.href = invoiceData.result.payment.qrCodeUrl;
        } else {
            showCustomNotification({
                type: "error",
                message: "Payment is not available",
            });
        }
    };

    return (
        <Card className="mb-4 order-card hover:shadow-md transition-shadow duration-300 w-100">
            <Space direction="vertical" className="w-100">
                <Row gutter={[16, 16]} justify="space-between" align="middle">
                    <Col xs={24} sm={24} md={12} lg={8}>
                        <Space align="center">
                            <ShoppingOutlined className="text-lg" />
                            <Text strong>
                                Order Code:{' '}
                                <Text strong type="danger">{invoiceData?.result?.invoiceNumber}</Text>
                            </Text>
                        </Space>
                    </Col>
                    {invoiceData?.result?.payment?.paymentStatus && (
                        <Col xs={24} sm={24} md={12} lg={8} className="m-0">
                            <Space align="center">
                                <CreditCardOutlined className="text-lg" />
                                <Text strong>Payment:</Text>
                                <Tag color={getStatusColor(invoiceData.result.payment.paymentStatus)}>
                                    {invoiceData.result.payment.paymentStatus}
                                </Tag>
                            </Space>
                        </Col>
                    )}
                    <Col xs={24} sm={24} md={12} lg={8}>
                        <Space align="center">
                            <TagOutlined className="text-lg" />
                            <Text strong>Order: </Text>
                            <Tag color={getStatusColor(order.status)}>{order.status}</Tag>
                        </Space>
                    </Col>
                </Row>

                <Row gutter={[16, 16]} className="mt-1">
                    <Col xs={24} sm={24} md={12} lg={8}>
                        <Space direction="vertical" className="w-100">
                            <div className="flex items-center gap-1">
                                <ClockCircleOutlined />
                                <Text type="secondary" strong>
                                    Ordered on: {formatDate(order.createdAt)}
                                </Text>
                            </div>
                            {!expanded && (
                                <Space>
                                    <Image
                                        src={order.orderItems[0].productResponse.mainImage}
                                        alt={order.orderItems[0].productResponse.name}
                                        width={40}
                                        height={40}
                                        className="object-cover rounded"
                                    />
                                    <div>
                                        <Text strong>{order.orderItems[0].productResponse.name}</Text>
                                        <div>
                                            <Text type="secondary">
                                                Size: {order.orderItems[0].size.name} × {order.orderItems[0].quantity}
                                            </Text>
                                        </div>
                                    </div>
                                    {order.orderItems.length > 1 && (
                                        <Badge count={`+${order.orderItems.length - 1}`} />
                                    )}
                                </Space>
                            )}

                            {expanded && (
                                <List
                                    className="mt-4"
                                    dataSource={order.orderItems}
                                    renderItem={(item: OrderItemResponse) => (
                                        <List.Item>
                                            <Space>
                                                <Image
                                                    src={item.productResponse.mainImage}
                                                    alt={item.productResponse.name}
                                                    width={40}
                                                    height={40}
                                                    className="rounded"
                                                />
                                                <div>
                                                    <Text strong>{item.productResponse.name}</Text>
                                                    <div>
                                                        <Text type="secondary">
                                                            Size: {item.size.name} × {item.quantity}
                                                        </Text>
                                                    </div>
                                                    <div>
                                                        <Text type="success">
                                                            {formatPrice(item.price)}
                                                        </Text>
                                                        {item?.promotion && (
                                                            <Tag color="red" className="ml-2">
                                                                -{item.promotion.discountPercentage}%
                                                            </Tag>
                                                        )}
                                                    </div>
                                                </div>
                                            </Space>
                                        </List.Item>
                                    )}
                                />
                            )}
                        </Space>
                    </Col>
                </Row>

                <Divider className="my-3" />

                <Row justify="space-between" align="middle">
                    <Col xs={12} md={8}>
                        <Space>
                            <Text>Total:</Text>
                            <Text strong className="text-lg">
                                {formatPrice(order.totalPriceAfterDiscount)}
                            </Text>
                            {order.totalPriceBeforeDiscount > order.totalPriceAfterDiscount && (
                                <Text delete type="secondary">
                                    {formatPrice(order.totalPriceBeforeDiscount)}
                                </Text>
                            )}
                        </Space>
                    </Col>
                    <Col xs={12} md={8} className="text-right">
                        <Button
                            type="link"
                            onClick={() => setExpanded(!expanded)}
                        >
                            {expanded ? 'Show Less' : 'View Details'}
                        </Button>
                        {invoiceData?.result?.payment?.paymentStatus === "PENDING" && (
                            <Button
                                type="link"
                                onClick={handlePayImmediately}
                            >
                                Pay Now
                            </Button>
                        )}
                    </Col>
                </Row>
            </Space>
        </Card>
    );
};

export default OrderItem;
