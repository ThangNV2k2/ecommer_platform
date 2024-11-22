import React, {useEffect, useState} from 'react';
import {Layout, Menu, Button, Row, Col, Drawer, Image, Badge, Spin, Input, Dropdown} from 'antd';
import logo from './img/logo.png';
import {
    FacebookOutlined,
    InstagramOutlined,
    PhoneOutlined,
    MailOutlined,
    MenuOutlined, SearchOutlined, UserOutlined, ShoppingCartOutlined,
} from '@ant-design/icons';
import {Route, BrowserRouter as Router, Routes, useNavigate, useSearchParams} from 'react-router-dom';
import Registration from './component/Auth/Register';
import {Account} from "./component/Auth/Account";
import Login from "./component/Auth/Login";
import OAuth2RedirectHandler from "./component/Auth/OAuth2RedirectHandler";
import {useDispatch, useSelector} from "react-redux";
import {useLazyGetUserInfoQuery} from "./redux/api/user-api";
import {setUser} from "./redux/slice/userSlice";
import {RootState} from "./redux/store";
import HomePage from "./component/Home/HomePage";
import ProductDetail from "./component/Home/ProductDetail";
import {useGetCartQuery} from "./redux/api/cart";
import {setCart} from "./redux/slice/cartSlice";
import Cart from "./component/Home/Cart";
import ShippingAddress from "./component/Auth/ShippingAddress";
import Checkout from './component/Home/Checkout';
import Contact from './component/Contact/Contact';
import About from './component/About/About';
import DebouncedInput from './utils/DebouncedInput';
import { useGetAllCategoryQuery } from './redux/api/category-api';

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
    const cart = useSelector((state: RootState) => state.cart.cart);
    const [getUserInfo, {isFetching: isFetchingUser}] = useLazyGetUserInfoQuery();
    const {data: cartData} = useGetCartQuery(userInfo?.id ?? "", {
        skip: !userInfo?.id
    });
    const [searchParams, setSearchParams] = useSearchParams();
    const { data: categoriesData, isFetching: categoriesIsFetching } = useGetAllCategoryQuery();

    const [showSearch, setShowSearch] = useState(false);
    const [searchValue, setSearchValue] = useState("");

    const handleChangeSearchValue = (value: string) => {
        setSearchValue(value);
        const categoryId = searchParams.get('categoryId') || '';
        setSearchParams({ categoryId, search: searchValue });
    }

    const renderCategories = () => {
        if (categoriesIsFetching) {
            return <Spin size="large" />;
        }

        if (categoriesData?.result && categoriesData.result.length > 0) {
            return categoriesData.result.map((category) => (
                <Menu.Item key={category.id} onClick={() => navigate(`?categoryId=${category.id}`)}>
                    {category.name.toUpperCase()}
                </Menu.Item>
            ));
        }

        return <Menu.Item disabled>No categories available</Menu.Item>;
    };

    const shopMenu = (
        <Menu>
            {renderCategories()}
        </Menu>
    );

    const handleGetUserInfo = async () => {
        try {
            const result = await getUserInfo().unwrap();
            if (result?.result) {
                dispatch(setUser(result.result));
            }
        } catch (error) {
            console.error("Error getting user info:", error);
        }
    }

    const handleNavigateAccount = () => {
        if (userInfo) {
            navigate('/account');
        } else {
            navigate('/account/login');
        }
    }

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token && !userInfo) {
            void handleGetUserInfo();
        }
    }, [dispatch, getUserInfo, userInfo]);

    useEffect(() => {
        if (cartData?.result) {
            dispatch(setCart(cartData.result));
        }
    }, [cartData?.result, dispatch]);

    return (
        <div className="container">
            <Layout>
                <Header className="bg-white border-radius-10 border-1 height-100">
                    <Row align="middle" justify="center" className="h-100" gutter={[24, 24]}>
                        <Col xs={24} sm={24} md={18} lg={18} xl={18}>
                            <Row align="middle" justify="space-between" gutter={[24, 24]}>
                                <Col xs={0} sm={0} md={6} lg={6} xl={6} className="flex align-center h-100">
                                    <Button
                                        type="primary"
                                        shape="circle"
                                        icon={<PhoneOutlined style={{fontSize: 24}}/>}
                                    />
                                    <span className="text-primary fs-16 fw-600 ml-1">0373357405</span>
                                </Col>

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
                                    {!showSearch ? (
                                        <Button type="text" shape="circle"
                                            icon={<SearchOutlined className="text-primary" style={{ fontSize: 24 }} />} 
                                            onClick={() => setShowSearch(true)}
                                            />
                                    ): (
                                        <DebouncedInput
                                            placeholder="Search"
                                            value={searchValue}
                                            onDebouncedChange={handleChangeSearchValue}
                                            delay={500}
                                        />
                                    )}
                                    <Button type="text" shape="circle"
                                            icon={<UserOutlined className="text-primary" style={{fontSize: 24}}/>}
                                            onClick={handleNavigateAccount}/>
                                    <Badge count={cart?.cartItems.length ?? 0} showZero>
                                        <Button type="text" shape="circle"
                                                icon={<ShoppingCartOutlined className="text-primary"
                                                                            onClick={() => navigate('/cart')}
                                                                            style={{fontSize: 24}}/>}/>
                                    </Badge>
                                </Col>
                            </Row>
                        </Col>
                    </Row>
                </Header>


                {/*<Drawer title="Menu" placement="right" onClose={closeDrawer} visible={drawerVisible}*/}
                {/*        bodyStyle={{padding: 0}}>*/}
                {/*    <Menu mode="vertical" defaultSelectedKeys={['home']}>*/}
                {/*        <Menu.Item key="home">Home</Menu.Item>*/}
                {/*        <Menu.Item key="shop">Shop</Menu.Item>*/}
                {/*        <Menu.Item key="blog">Blog</Menu.Item>*/}
                {/*        <Menu.Item key="contact">Contact</Menu.Item>*/}
                {/*        <Menu.Item key="about">About</Menu.Item>*/}
                {/*    </Menu>*/}
                {/*</Drawer>*/}

                <Content className="bg-white w-100">
                    <Row align="middle" justify="center" className="height-50" gutter={[24, 24]}>
                        <Col xs={24} sm={24} md={18} lg={24} xl={24} className="w-100 flex justify-center">
                            <Menu mode="horizontal" defaultSelectedKeys={['home']} className="fw-500">
                                <Menu.Item key="home" onClick={() => navigate('/')}>HOME</Menu.Item>
                                <Dropdown overlay={shopMenu} trigger={['hover']}>
                                    <Menu.Item key="shop">SHOP</Menu.Item>
                                </Dropdown>
                                <Menu.Item key="blog" onClick={() => navigate('/blog')}>BLOG</Menu.Item>
                                <Menu.Item key="contact" onClick={() => navigate('/contact')}>CONTACT</Menu.Item>
                                <Menu.Item key="about" onClick={() => navigate('/about')}>ABOUT</Menu.Item>
                                <Menu.Item key="group" onClick={() => navigate('/group')}>GROUP</Menu.Item>
                            </Menu>
                        </Col>
                    </Row>
                    <Row align="middle" justify="center" gutter={[24, 24]} className="my-5">
                        {
                            isFetchingUser ? (
                                <div className="flex justify-center w-100">
                                    <Spin size="large" />
                                </div>
                            ) : (
                                    <Col xs={24} sm={24} md={24} lg={24} xl={18}>
                                        <Routes>
                                            <Route path="/" element={<HomePage />} />
                                            <Route path="/product/:id" element={<ProductDetail />} />
                                            <Route path="/cart" element={<Cart />} />
                                            <Route path="/account" element={<Account />}>
                                                <Route index element={<Account />} />
                                            </Route>
                                            <Route path="/contact" element={<Contact />} />
                                            <Route path="account/address" element={<ShippingAddress />} />
                                            <Route path="checkout" element={<Checkout />} />
                                            <Route path="/about" element={<About />} />
                                            <Route path="/account/login" element={<Login />} />
                                            <Route path="/account/register" element={<Registration />} />
                                            <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
                                        </Routes>
                                    </Col>
                           )
                        }
                    </Row>
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