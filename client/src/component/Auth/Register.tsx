import React from 'react';
import {Form, Input, Button, Row, Col} from 'antd';
import { Formik, Field, Form as FormikForm, FieldProps } from 'formik';
import * as Yup from 'yup';
import {useRegisterMutation} from "../../redux/api/auth-api";
import {useNavigate} from "react-router-dom";

interface FormValues {
    email: string;
    password: string;
    confirmPassword: string;
    name: string;
}

const validationSchema = Yup.object().shape({
    name: Yup.string()
        .min(5, 'Name must be at least 5 characters')
        .max(50, 'Name must be at most 50 characters')
        .required('Name is required'),
    email: Yup.string()
        .email('Email is invalid')
        .required('Email is required'),
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

const Registration: React.FC = () => {
    const navigate = useNavigate();
    const [registerAccountEmail] = useRegisterMutation();
    const initialValues: FormValues = {
        email: '',
        password: '',
        confirmPassword: '',
        name: '',
    };

    const handleSubmit = async (values: FormValues) => {
        const result = await registerAccountEmail({
            email: values.email,
            password: values.password,
            name: values.name,
        }).unwrap();
        if(result?.result) {
            navigate('/account/login');
        }
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
                            <FormikForm className="h-100 w-100">
                                <Form.Item
                                    validateStatus={errors.name && touched.name ? 'error' : ''}
                                    help={touched.name && errors.name}
                                >
                                    <Field name="name">
                                        {({ field }: FieldProps) => (
                                            <Input {...field} placeholder="Full Name" prefix={<i className="anticon anticon-user" />} />
                                        )}
                                    </Field>
                                </Form.Item>
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
                                            <Input.Password {...field} placeholder="Password" prefix={<i className="anticon anticon-lock" />} />
                                        )}
                                    </Field>
                                </Form.Item>

                                <Form.Item
                                    validateStatus={errors.confirmPassword && touched.confirmPassword ? 'error' : ''}
                                    help={touched.confirmPassword && errors.confirmPassword}
                                >
                                    <Field name="confirmPassword">
                                        {({ field }: FieldProps) => (
                                            <Input.Password {...field} placeholder="Confirm Password" prefix={<i className="anticon anticon-lock" />} />
                                        )}
                                    </Field>
                                </Form.Item>

                                <Form.Item>
                                    <Button type="primary" htmlType="submit" block>
                                        Register
                                    </Button>
                                </Form.Item>
                            </FormikForm>
                        )}
                    </Formik>

                </Col>
            </Row>
        </div>
    );
};

export default Registration;
