import { Row, Col, Typography, Card } from 'antd';
import { useSelector } from 'react-redux';
import {RootState} from "../../redux/store";

const { Title, Text, Link } = Typography;

export const Account = () => {
    const userInfo = useSelector((state: RootState) => state.user.user);

    return (
        <div className="account-page">
            <Row gutter={[24, 24]} justify="center" className="mt-4">
                <Col xs={24} sm={24} md={12} lg={12} xl={12}>
                    <Card>
                        <Title level={4}>Tài khoản của bạn</Title>
                        <Text>Bạn chưa đặt mua sản phẩm.</Text>
                    </Card>
                </Col>

                <Col xs={24} sm={24} md={12} lg={12} xl={12}>
                    <Card>
                        <div className="account-info">
                            <div className="account-info-header">
                                <Title level={4}>Thông tin tài khoản</Title>
                                <Link href="/logout" className="logout-link">
                                    Thoát
                                </Link>
                            </div>
                            <div className="account-info-details">
                                <Text>
                                    <strong>Họ tên</strong> : {userInfo?.name ?? ""}
                                </Text>
                                <br />
                                <Text>
                                    <strong>Email</strong> : {userInfo?.email ?? ""}
                                </Text>
                                <br />
                                <br />
                                <Link href="/account/address">
                                    Xem địa chỉ <span style={{ fontSize: 16 }}>➡</span>
                                </Link>
                            </div>
                        </div>
                    </Card>
                </Col>
            </Row>
        </div>
    );
};