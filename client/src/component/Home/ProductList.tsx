import {Col, Row, Select, Typography} from "antd";
import {ProductResponse} from "../../types/product";
import ProductItem from "./ProductItem";
import {Option} from "antd/es/mentions";

const ProductList = ({ products }: { products: ProductResponse[]}) => {

    return (
        <div>
            <Row justify="space-between" align="middle" className="mx-4 background-card py-1 ">
                <Col>
                    <p className="fw-600 fs-18 m-0">
                        T-SHIRTS
                    </p>
                </Col>

                <Col>
                    <div style={{display: 'flex', alignItems: 'center'}}>
                        <span style={{marginRight: 8}}>Sort by:</span>
                        <Select defaultValue="newest" style={{width: 120}}>
                            <Option value="newest">Newest</Option>
                            <Option value="oldest">Oldest</Option>
                            <Option value="priceAsc">Price Ascending</Option>
                            <Option value="priceDesc">Price Descending</Option>
                        </Select>
                    </div>
                </Col>
            </Row>
            <Row gutter={[16, 16]} className="flex flex-wrap p-4 justify-space-between">
                {products.map((product) => (
                    <Col key={product.id}>
                        <ProductItem product={product}/>
                    </Col>
                ))}
            </Row>
        </div>
    );
};

export default ProductList;