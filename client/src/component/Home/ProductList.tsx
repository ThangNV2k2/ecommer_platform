import {Badge, Card, Col, Row, Select, Typography} from "antd";
import {ProductResponse} from "../../types/product";
import ProductItem from "./ProductItem";
import {Option} from "antd/es/mentions";

const { Title } = Typography;
const ProductList = ({ products }: { products: ProductResponse[]}) => {

    return (
        <div>
            <Row justify="space-between" align="middle" className="px-4 background-card">
                <Col>
                    <Title level={4} style={{ margin: 0 }}>
                        T-SHIRTS
                    </Title>
                </Col>

                <Col>
                    <div style={{ display: 'flex', alignItems: 'center' }}>
                        <span style={{ marginRight: 8 }}>Sắp xếp theo:</span>
                        <Select defaultValue="newest" style={{ width: 120 }}>
                            <Option value="newest">Mới nhất</Option>
                            <Option value="oldest">Cũ nhất</Option>
                            <Option value="priceAsc">Giá tăng dần</Option>
                            <Option value="priceDesc">Giá giảm dần</Option>
                        </Select>
                    </div>
                </Col>
            </Row>
            <Row gutter={[16, 16]} className="flex flex-wrap p-4">
                {products.map((product) => (
                    <Col key={product.id} span={8}>
                        <ProductItem product={product} />
                    </Col>
                ))}
            </Row>
        </div>
    );
};

export default ProductList;