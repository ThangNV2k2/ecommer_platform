import React, {useEffect, useState} from 'react';
import {Layout, Menu, Button, Row, Col, Drawer, Image, Badge} from 'antd';
import logo from './img/logo.png';
import './App.scss';
import {
    FacebookOutlined,
    InstagramOutlined,
    PhoneOutlined,
    MailOutlined,
    MenuOutlined, SearchOutlined, UserOutlined, ShoppingCartOutlined,
} from '@ant-design/icons';
import {Navigate, Route, BrowserRouter as Router, Routes, useNavigate} from 'react-router-dom';
import Registration from './component/Auth/Register';
import {Account} from "./component/Auth/Account";
import Login from "./component/Auth/Login";
import OAuth2RedirectHandler from "./component/Auth/OAuth2RedirectHandler";
import {useDispatch, useSelector} from "react-redux";
import {useLazyGetUserInfoQuery} from "./redux/api/user-api";
import {setUser} from "./redux/slice/userSlice";
import {RootState} from "./redux/store";
import ProtectedRoute from "./component/Auth/ProtectedRoute";

const {Header, Content, Footer} = Layout;

const App = () => {
    const navigate = useNavigate();
    const [drawerVisible, setDrawerVisible] = useState(false);

    const showDrawer = () => {
        setDrawerVisible(true);
    };

    const closeDrawer = () => {
        setDrawerVisible(false);
    };

    const dispatch = useDispatch();
    const userInfo = useSelector((state: RootState) => state.user.user);
    const [getUserInfo] = useLazyGetUserInfoQuery();

    const handleGetUserInfo = async () => {
        debugger;
        try {
            const result = await getUserInfo().unwrap();
            if(result?.result) {
                dispatch(setUser(result.result));
            }
        } catch (error) {
            console.error("Error getting user info:", error);
        }
    }

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token && !userInfo) {
            void handleGetUserInfo();
        }
    }, [dispatch, getUserInfo, userInfo]);

    return (
        <div className="container">
            <Layout>
                <Header className="bg-white border-radius-10 border-1 height-100">
                    <Row align="middle" justify="space-between" className="h-100" gutter={[24, 24]}>
                        {/* Số điện thoại, chỉ hiển thị trên màn hình lớn */}
                        <Col xs={0} sm={0} md={6} lg={6} xl={6} className="flex align-center h-100">
                            <Button
                                type="primary"
                                shape="circle"
                                icon={<PhoneOutlined style={{ fontSize: 24 }} />}
                            />
                            <span className="text-primary fs-16 fw-600 ml-1">0373357405</span>
                        </Col>

                        {/* Logo, luôn hiển thị trên mọi màn hình */}
                        <Col xs={16} sm={16} md={6} lg={6} xl={6} className="flex justify-center align-center">
                            <Image
                                src={logo}
                                alt="logo"
                                className="img-min-h-85"
                                preview={false}
                                onClick={() => navigate('/')}
                            />
                        </Col>

                        <Col xs={8} sm={8} md={6} lg={6} xl={6} className="flex justify-end align-center gap-1">
                            <Button type="text" shape="circle" icon={<SearchOutlined className="text-primary" style={{ fontSize: 24 }} />} />
                            <Button type="text" shape="circle" icon={<UserOutlined className="text-primary" style={{ fontSize: 24 }} />} onClick={() => navigate('/account')} />
                            <Badge count={0} showZero>
                                <Button type="text" shape="circle" icon={<ShoppingCartOutlined className="text-primary" style={{ fontSize: 24 }} />} />
                            </Badge>
                        </Col>
                    </Row>

                    <Row>
                        <Col xs={24} sm={24} md={24} lg={24} xl={24} className="sticky-menu w-100 flex justify-center">
                            <Menu mode="horizontal" defaultSelectedKeys={['home']} className="menu-large">
                                <Menu.Item key="home" onClick={() => navigate('/')}>Home</Menu.Item>
                                <Menu.Item key="shop" onClick={() => navigate('/shop')}>Shop</Menu.Item>
                                <Menu.Item key="blog" onClick={() => navigate('/blog')}>Blog</Menu.Item>
                                <Menu.Item key="contact" onClick={() => navigate('/contact')}>Contact</Menu.Item>
                                <Menu.Item key="about" onClick={() => navigate('/about')}>About</Menu.Item>
                            </Menu>
                        </Col>
                    </Row>
                </Header>


                <Drawer title="Menu" placement="right" onClose={closeDrawer} visible={drawerVisible}
                        bodyStyle={{padding: 0}}>
                    <Menu mode="vertical" defaultSelectedKeys={['home']}>
                        <Menu.Item key="home">Home</Menu.Item>
                        <Menu.Item key="shop">Shop</Menu.Item>
                        <Menu.Item key="blog">Blog</Menu.Item>
                        <Menu.Item key="contact">Contact</Menu.Item>
                        <Menu.Item key="about">About</Menu.Item>
                    </Menu>
                </Drawer>

                <Content className="bg-white">
                        <Routes>
                            <Route path="/account" element={<Account />}>
                                <Route index element={<Account />} />
                            </Route>

                            <Route path="/account/login" element={<Login />} />
                            <Route path="/account/register" element={<Registration />} />
                            <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
                        </Routes>

                </Content>

                <Footer className="bg-primary text-white">
                    <Row gutter={[24, 24]} justify="space-around">
                        <Col xs={24} sm={12} md={8} lg={6} xl={6}>
                            <h4>Thông tin liên hệ</h4>
                            <p>
                                <PhoneOutlined/> Phone: 037 335 7405
                            </p>
                            <p>
                                <MailOutlined/> Email: vergency.contact@gmail.com
                            </p>
                        </Col>
                        <Col xs={24} sm={12} md={8} lg={6} xl={6}>
                            <h4>Chính sách hỗ trợ</h4>
                            <ul>
                                <li>Tìm kiếm</li>
                                <li>Giới thiệu</li>
                                <li>Chính sách đổi trả</li>
                                <li>Chính sách bảo mật</li>
                                <li>Điều khoản dịch vụ</li>
                            </ul>
                        </Col>
                        <Col xs={24} sm={12} md={8} lg={6} xl={6}>
                            <h4>Thông tin liên kết</h4>
                            <div>
                                <FacebookOutlined style={{fontSize: '24px', marginRight: '10px', color: '#1890ff'}}/>
                                <InstagramOutlined style={{fontSize: '24px', color: '#1890ff'}}/>
                            </div>
                        </Col>
                    </Row>
                    <div>Bản quyền thuộc về VERGENCY</div>
                </Footer>
            </Layout>
        </div>
    );
};

export default App;