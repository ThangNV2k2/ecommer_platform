import {useCreateShippingAddressMutation, useUpdateShippingAddressMutation} from "../../redux/api/shipping-address";
import {RootState} from "../../redux/store";
import {useSelector} from "react-redux";
import * as Yup from 'yup';
import {Button, Checkbox, Form, Input, Modal, Select} from "antd";
import {ShippingAddressResponse} from "../../types/shipping-address";
import {Field, FieldProps, Form as FormikForm, Formik} from 'formik';
import {PhoneOutlined, UserOutlined} from '@ant-design/icons';
import "../../sass/shipping-address.scss";
import {showCustomNotification} from "../../utils/notification";

const ShippingAddressSchema = Yup.object().shape({
    recipientName: Yup.string().required("Recipient Name is required"),
    phoneNumber: Yup.string().required("Phone Number is required"),
    addressDetail: Yup.string().required("Address Detail is required"),
    country: Yup.string().required("Country is required"),
});

interface ShippingAddress {
    recipientName: string;
    phoneNumber: string;
    addressDetail: string;
    country: string;
    isDefault: boolean;
}

interface ShippingAddressFormProps {
    editingAddress?: ShippingAddressResponse;
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
}

const CreateOrUpdateShippingAddressModal: React.FC<ShippingAddressFormProps> = ({
                                                                                    editingAddress,
                                                                                    isOpen,
                                                                                    onClose,
                                                                                    onSuccess,
                                                                                }) => {
    const userInfo = useSelector((state: RootState) => state.user.user);
    const [createShippingAddress] = useCreateShippingAddressMutation();
    const [updateShippingAddress] = useUpdateShippingAddressMutation();
    const initialValues: ShippingAddress = {
        recipientName: editingAddress?.recipientName ?? "",
        phoneNumber: editingAddress?.phoneNumber ?? "",
        addressDetail: editingAddress?.addressDetail ?? "",
        country: editingAddress?.country ?? "",
        isDefault: editingAddress?.isDefault ?? false,
    };

    const handleFormSubmit = async (values: ShippingAddress) => {
        if (editingAddress) {
            await updateShippingAddress({
                id: editingAddress.id, shippingAddressRequest: {
                    userId: userInfo?.id ?? "",
                    ...values,
                }
            }).unwrap().then((result) => {
                showCustomNotification({
                    type: "success",
                    message: result?.message ?? "Update shipping address successfully",
                });
                onSuccess();
            }).catch((error) => {
                showCustomNotification({
                    type: "error",
                    message: error.data.message,
                });
            });
        } else {
            await createShippingAddress({
                userId: userInfo?.id ?? "",
                ...values,
            }).unwrap().then((result) => {
                showCustomNotification({
                    type: "success",
                    message: result?.message ?? "Create shipping address successfully",
                });
                onSuccess();
            }).catch((error) => {
                showCustomNotification({
                    type: "error",
                    message: error.data.message,
                });
            });
            ;
        }
    };

    return (
        <Modal
            title={editingAddress ? "Edit Address" : "Add New Address"}
            open={isOpen}
            onCancel={onClose}
            footer={null}
            width={500}
        >
            <Formik
                initialValues={initialValues}
                validationSchema={ShippingAddressSchema}
                onSubmit={handleFormSubmit}
                enableReinitialize
            >
                {({errors, touched}) => (
                    <FormikForm>
                        <Form.Item
                            validateStatus={errors.recipientName && touched.recipientName ? 'error' : ''}
                            help={touched.recipientName && errors.recipientName}
                            className="mb-2"
                        >
                            <Field name="recipientName">
                                {({field}: FieldProps) => (
                                    <Input
                                        {...field}
                                        placeholder="Enter recipient name"
                                        prefix={<UserOutlined className="mr-1"/>}
                                    />
                                )}
                            </Field>
                        </Form.Item>

                        <Form.Item
                            validateStatus={errors.phoneNumber && touched.phoneNumber ? 'error' : ''}
                            help={touched.phoneNumber && errors.phoneNumber}
                            className="mb-2"
                        >
                            <Field name="phoneNumber">
                                {({field}: FieldProps) => (
                                    <Input
                                        {...field}
                                        placeholder="Enter phone number"
                                        prefix={<PhoneOutlined className="mr-1"/>}
                                    />
                                )}
                            </Field>
                        </Form.Item>

                        <Form.Item
                            validateStatus={errors.addressDetail && touched.addressDetail ? 'error' : ''}
                            help={touched.addressDetail && errors.addressDetail}
                            className="mb-2"
                        >
                            <Field name="addressDetail">
                                {({field}: FieldProps) => (
                                    <Input.TextArea
                                        {...field}
                                        placeholder="Enter address detail"
                                        rows={3}
                                    />
                                )}
                            </Field>
                        </Form.Item>

                        <Form.Item
                            validateStatus={errors.country && touched.country ? 'error' : ''}
                            help={touched.country && errors.country}
                            className="mb-2"
                        >
                            <Field name="country">
                                {({field, form}: FieldProps) => (
                                    <Select placeholder="Select country"
                                            onChange={(value) => form.setFieldValue('country', value)}
                                            onBlur={() => form.setFieldTouched('country', true)}
                                            value={field.value || "Vietnam"}
                                    >
                                        <Select.Option value="Vietnam">Vietnam</Select.Option>
                                        <Select.Option value="China">China</Select.Option>
                                        <Select.Option value="USA">USA</Select.Option>
                                        <Select.Option value="Japan">Japan</Select.Option>
                                        <Select.Option value="Korea">Korea</Select.Option>

                                    </Select>
                                )}
                            </Field>
                        </Form.Item>

                        <Form.Item className="mb-2">
                            <Field name="isDefault">
                                {({field}: FieldProps) => (
                                    <Checkbox {...field}>
                                        Set as default address
                                    </Checkbox>
                                )}
                            </Field>
                        </Form.Item>

                        <Form.Item className="mb-2">
                            <Button type="primary" htmlType="submit" block>
                                {editingAddress ? "Update" : "Add"} Address
                            </Button>
                        </Form.Item>

                        <Form.Item className="mb-2">
                            <Button block onClick={onClose}>
                                Cancel
                            </Button>
                        </Form.Item>
                    </FormikForm>
                )}
            </Formik>
        </Modal>
    );
};

export default CreateOrUpdateShippingAddressModal;