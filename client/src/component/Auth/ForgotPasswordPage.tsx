import React, { useState } from 'react';
import { Form, Input, Button, Row, Col, Typography, message } from 'antd';
import { Formik, Field, Form as FormikForm, FieldProps } from 'formik';
import * as Yup from 'yup';
import { LockOutlined, MailOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { useChangePasswordMutation, useLazySendOtpQuery } from '../../redux/api/auth-api';

const { Text, Title, Link } = Typography;

interface FormValues {
    email: string;
    password: string;
    confirmPassword: string;
    verificationCode: string;
}

const isValidEmail = (email: string): boolean => {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    return emailRegex.test(email);
};

const validationSchema = Yup.object().shape({
    email: Yup.string()
        .email('Email is invalid')
        .required('Email is required'),
    verificationCode: Yup.string()
        .length(6, 'Verification code must be 6 digits')
        .matches(/^[0-9]+$/, 'Otp code must be numeric')
        .required('Otp is required'),
    password: Yup.string()
        .required('Password is required')
        .matches(
            /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\W)(?!.* ).{8,16}$/,
            'Password must contain an uppercase, a lowercase, a number, a special character and be 8-16 characters long'
        ),
    confirmPassword: Yup.string()
        .oneOf([Yup.ref('password'), ""], 'Passwords must match')
        .required('Confirm Password is required'),
});

const ForgotPassword: React.FC = () => {
    const navigate = useNavigate();
    const [isOtpSent, setIsOtpSent] = useState(false);

    const [sendOtp, { isFetching: isOtpLoading }] = useLazySendOtpQuery();
    const [changePassword, { isLoading: isLoadingChangePassword }] = useChangePasswordMutation();

    const initialValues: FormValues = {
        email: '',
        password: '',
        confirmPassword: '',
        verificationCode: '',
    };

    const handleSendOtp = (email: string) => {
        if (!email || !email.trim() || !isValidEmail(email)) {
            message.error('Please enter a valid email address');
            return;
        }

        sendOtp(email)
            .unwrap()
            .then((res) => {
                setIsOtpSent(true);
                message.success(res.message ?? "Otp sent successfully");
            })
            .catch((error) => {
                message.error(error.data.message);
            });
    };

    const handleSubmit = async (values: FormValues) => {
        const { email, password, verificationCode } = values;

        changePassword({
            email,
            password,
            otp: verificationCode,
        })
            .unwrap()
            .then((res) => {
                message.success(res.message ?? "Password changed successfully");
                navigate('/account/login');
            })
            .catch((error) => {
                message.error(error.data.message);
            });
    };

    return (
        <div className="auth-layout">
            <Row align="middle" justify="center" gutter={[24, 24]} className="h-100">
                <Col xs={20} sm={20} md={12} lg={8} xl={8} className="flex justify-center align-center">
                    <Formik initialValues={initialValues} validationSchema={validationSchema} onSubmit={handleSubmit}>
                        {({ errors, touched, setFieldValue, values }) => (
                            <FormikForm className="login-form">
                                <Title level={5} className="text-center mt-0 pb-2">Forgot Password</Title>

                                <Form.Item
                                    validateStatus={errors.email && touched.email ? 'error' : ''}
                                    help={touched.email && errors.email}
                                >
                                    <Field name="email">
                                        {({ field }: FieldProps) => (
                                            <Input
                                                {...field}
                                                placeholder="Email"
                                                prefix={<MailOutlined className="mr-1" />}
                                                disabled={isOtpSent}
                                            />
                                        )}
                                    </Field>
                                </Form.Item>

                                {!isOtpSent ? (
                                    <Form.Item>
                                        <Button
                                            type="primary"
                                            onClick={() => handleSendOtp(values.email)}
                                            loading={isOtpLoading}
                                            block
                                        >
                                            Send OTP
                                        </Button>
                                    </Form.Item>
                                ) : (
                                    <>
                                        <Form.Item
                                            validateStatus={errors.password && touched.password ? 'error' : ''}
                                            help={touched.password && errors.password}
                                        >
                                            <Field name="password">
                                                {({ field }: FieldProps) => (
                                                    <Input.Password {...field} placeholder="New Password" prefix={<LockOutlined className="mr-1" />} />
                                                )}
                                            </Field>
                                        </Form.Item>

                                        <Form.Item
                                            validateStatus={errors.confirmPassword && touched.confirmPassword ? 'error' : ''}
                                            help={touched.confirmPassword && errors.confirmPassword}
                                        >
                                            <Field name="confirmPassword">
                                                {({ field }: FieldProps) => (
                                                    <Input.Password {...field} placeholder="Confirm New Password" prefix={<LockOutlined className="mr-1" />} />
                                                )}
                                            </Field>
                                        </Form.Item>

                                        <Form.Item
                                            validateStatus={errors.verificationCode && touched.verificationCode ? 'error' : ''}
                                            help={touched.verificationCode && errors.verificationCode}
                                        >
                                            <div className='flex justify-space-between'>
                                                {[...Array(6)].map((_, index) => (
                                                    <Input
                                                        key={index}
                                                        maxLength={1}
                                                        onChange={(e) => {
                                                            const value = e.target.value;
                                                            const newCode = values.verificationCode.split('');
                                                            newCode[index] = value;
                                                            setFieldValue('verificationCode', newCode.join(''));
                                                            if (value && index < 5) {
                                                                const nextInput = document.getElementById(`otp-${index + 1}`);
                                                                if (nextInput) {
                                                                    nextInput.focus();
                                                                }
                                                            }
                                                        }}
                                                        id={`otp-${index}`}
                                                        placeholder="0"
                                                        style={{ width: '40px', textAlign: 'center' }}
                                                    />
                                                ))}
                                            </div>
                                        </Form.Item>

                                        <Form.Item>
                                            <Button type="primary" htmlType="submit" className="fw-600" loading={isLoadingChangePassword} block>
                                                Reset Password
                                            </Button>
                                        </Form.Item>
                                    </>
                                )}

                                <Form.Item className='text-center'>
                                    <Text>Remember your password?</Text> <Link href="/account/login">Login now</Link>
                                </Form.Item>
                            </FormikForm>
                        )}
                    </Formik>
                </Col>
            </Row>
        </div>
    );
};

export default ForgotPassword;