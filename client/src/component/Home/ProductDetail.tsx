import {useNavigate, useParams} from 'react-router-dom';
import {useGetProductByIdQuery} from "../../redux/api/product-api";
import {useGetProductInventoryByProductIdQuery} from "../../redux/api/product-inventory";
import {Card, Image, Typography, Divider, Button, Select, InputNumber, Row, Col, Rate, Spin} from 'antd';
import {useAddCartItemMutation} from "../../redux/api/cart";
import {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import {showCustomNotification} from "../../utils/notification";
import {addCartItem} from "../../redux/slice/cartSlice";
import { calculateProductItem } from './services';

const {Title, Text} = Typography;
const {Option} = Select;

const ProductDetail = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const {data: product, isLoading: productLoading} = useGetProductByIdQuery(id ?? "", {skip: !id});
    const {
        data: productInventory,
        isLoading: productInventoryLoading
    } = useGetProductInventoryByProductIdQuery(id ?? "", {skip: !id});
    const cartData = useSelector((state: RootState) => state.cart.cart);
    const [addProductToCart, {isLoading: addProductToCartLoading}] = useAddCartItemMutation();

    const [selectedSizeId, setSelectedSizeId] = useState("");
    const [quantity, setQuantity] = useState(1);

    const handleAddProductToCart = async () => {
        const selectedInventory = productInventory?.result?.find((inventory) => inventory.size.id === selectedSizeId);

        if (!selectedInventory) {
            showCustomNotification({
                message: "Please select size",
                type: "error",
            });
            return;
        }

        if (quantity > selectedInventory.quantity) {
            showCustomNotification({
                message: "Quantity exceeds available quantity",
                type: "error",
            });
            return;
        }

        if (!cartData) {
            showCustomNotification({
                message: "Please login to add product to cart",
                type: "error",
            });
            return;
        }

        addProductToCart({
            cartId: cartData?.id ?? "",
            productId: id ?? "",
            sizeId: selectedSizeId,
            quantity: quantity,
        }).unwrap()
            .then((response) => {
                if (response?.result) {
                    dispatch(addCartItem(response.result));
                    showCustomNotification({
                        message: response.message,
                        type: "success",
                    });
                }
            })
            .catch((error) => {
                showCustomNotification({
                    message: error.data.message,
                    type: "error",
                });
            });
    }

    const handleBingNow = async () => {
        await handleAddProductToCart();
        navigate("/cart");
    }

    if (productLoading || productInventoryLoading) {
        return <Spin tip="Loading..."/>;
    }

    if (!product?.result) return <Text type="danger">Product not found</Text>;
    if (!productInventory?.result) return <Text type="danger">Product inventory not found</Text>;

    const discountPrice = calculateProductItem(product.result);

    return (
        <Row gutter={16} style={{padding: "20px"}}>
            <Col span={6}>
                {/* Sidebar với danh sách ảnh sản phẩm */}
            </Col>

            <Col span={12}>
                <Card cover={<Image src={product.result.mainImage} alt={product.result.name} preview/>}>
                    <Title level={3}>{product.result.name}</Title>
                    <Text type="secondary">SKU: SUN1-1</Text>
                    <Divider/>
                    <Rate disabled allowHalf defaultValue={product?.result.rating || 5} style={{fontSize: "16px"}}/>
                    <Text strong style={{fontSize: "24px", color: "red"}}>₫{discountPrice.toLocaleString()}</Text>
                    <Text delete style={{
                        fontSize: "16px",
                        marginLeft: "10px"
                    }}>₫{product?.result.price.toLocaleString()}</Text>
                    <Divider/>

                    <Text>Size:</Text>
                    <Select
                        className="ml-2"
                        placeholder="Select Size"
                        onChange={(value) => {
                            const selectedSize = productInventory?.result?.find((inventory) => inventory.size.name === value);
                            setSelectedSizeId(selectedSize ? selectedSize.size.id : "");
                        }}
                    >
                        {productInventory.result.map((inventory) => (
                            <Option key={inventory.size.id} value={inventory.size.name}>
                                {inventory.size.name.toLocaleUpperCase()}
                            </Option>
                        ))}
                    </Select>

                    <div style={{marginTop: "10px"}}>
                        <Text>Quantity:</Text>
                        <InputNumber
                            min={0}
                            max={productInventory.result.find((inventory) => inventory.size.id === selectedSizeId)?.quantity || 0}
                            defaultValue={1}
                            onChange={(value) => setQuantity(value ?? 0)}
                            style={{marginLeft: "10px"}}
                        />
                    </div>

                    <div style={{marginTop: "20px"}}>
                        <Button type="primary" onClick={handleAddProductToCart} loading={addProductToCartLoading}
                                style={{marginRight: "10px"}}>
                            Add to cart
                        </Button>
                        <Button type="default" onClick={handleBingNow}>Bring now</Button>
                    </div>
                </Card>
            </Col>
        </Row>
    );
};

export default ProductDetail;