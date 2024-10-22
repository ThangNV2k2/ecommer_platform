import React from 'react';
import {Form, Input, Button, Row, Col} from 'antd';
import { Formik, Field, Form as FormikForm, FieldProps } from 'formik';
import * as Yup from 'yup';
import { useNavigate } from 'react-router-dom';
import {baseApi, useLoginEmailMutation} from "../../redux/api/auth-api";
import {setUser} from "../../redux/slice/userSlice";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import {useLazyGetUserInfoQuery} from "../../redux/api/user-api";

interface FormValues {
    email: string;
    password: string;
}

const validationSchema = Yup.object().shape({
    email: Yup.string()
        .email('Email không hợp lệ')
        .required('Vui lòng nhập email'),
    password: Yup.string()
        .required('Vui lòng nhập mật khẩu')
});

const Login: React.FC = () => {
    const [login] = useLoginEmailMutation();
    const navigate = useNavigate();

    const initialValues: FormValues = {
        email: '',
        password: '',
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

    const handleSubmit = async (values: FormValues) => {
        try {
            const result = await login({
                email: values.email,
                password: values.password,
            }).unwrap();

            localStorage.setItem("token", result.result?.token ?? "");
            if (!userInfo) {
                await handleGetUserInfo();
            }
            navigate('/account');
        } catch (error) {
            console.error("Đăng nhập thất bại:", error);
        }
    };

    const handleGoogleLogin = async () => {
        window.location.href = `${baseApi}/oauth2/login/google`;
    };

    return (
        <div className="auth-layout">
            <Row align="middle" justify="center" gutter={[24, 24]} className="h-100">
                <Col
                    xs={20} sm={20} md={12} lg={8} xl={8}
                    className="flex justify-center align-center"
                >

                    <Formik
                        initialValues={initialValues}
                        validationSchema={validationSchema}
                        onSubmit={handleSubmit}
                    >
                        {({errors, touched}) => (
                            <FormikForm className="login-form">
                                <Form.Item
                                    validateStatus={errors.email && touched.email ? 'error' : ''}
                                    help={touched.email && errors.email}
                                >
                                    <Field name="email">
                                        {({ field }: FieldProps) => (
                                            <Input {...field} placeholder="Email" prefix={<i className="anticon anticon-mail" />} />
                                        )}
                                    </Field>
                                </Form.Item>

                                <Form.Item
                                    validateStatus={errors.password && touched.password ? 'error' : ''}
                                    help={touched.password && errors.password}
                                >
                                    <Field name="password">
                                        {({ field }: FieldProps) => (
                                            <Input.Password {...field} placeholder="Mật khẩu" prefix={<i className="anticon anticon-lock" />} />
                                        )}
                                    </Field>
                                </Form.Item>

                                <Form.Item>
                                    <Button type="primary" htmlType="submit" block>
                                        Đăng nhập
                                    </Button>
                                </Form.Item>

                                <Form.Item>
                                    <Button type="default" onClick={handleGoogleLogin} block>
                                        Đăng nhập bằng Google
                                    </Button>
                                </Form.Item>

                                <Form.Item>
                                    <a href="#">Quên mật khẩu?</a> hoặc <a href="/register">Đăng ký</a>
                                </Form.Item>
                            </FormikForm>
                        )}
                    </Formik>
                </Col>
            </Row>
        </div>
    );
};

export default Login;
