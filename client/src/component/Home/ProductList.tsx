import { Col, Row, Select, Spin, Typography } from "antd";
import { ProductResponse, ProductResponseKeys } from "../../types/product";
import ProductItem from "./ProductItem";
import { Option } from "antd/es/mentions";
import '../../sass/home-page.scss';
import { SortType } from "../../types/page";

const ProductList = ({ products, categoryName, setSortProduct, isFetchingProduct }: {
    products: ProductResponse[], categoryName: string, setSortProduct: (sortBy: {
        sortBy: ProductResponseKeys, sortDirection: SortType
    }) => void, isFetchingProduct: boolean
}) => {
    return (
        <div>
            <Row justify="space-between" align="middle" className="product-list-header">
                <Col>
                    <p className="fw-600 fs-18 m-0">
                        {categoryName}
                    </p>
                </Col>

                <Col>
                    <div className="flex align-center">
                        <span className="mr-2">Sort by:</span>
                        <Select defaultValue="newest" style={{ width: 120 }} onChange={(value) => {
                            switch (value) {
                                case 'newest':
                                    setSortProduct({ sortBy: 'createdAt', sortDirection: 'asc' });
                                    break;
                                case 'oldest':
                                    setSortProduct({ sortBy: 'createdAt', sortDirection: 'desc' });
                                    break;
                                case 'priceAsc':
                                    setSortProduct({ sortBy: 'price', sortDirection: 'asc' });
                                    break;
                                case 'priceDesc':
                                    setSortProduct({ sortBy: 'price', sortDirection: 'desc' });
                                    break;
                                default:
                                    setSortProduct({ sortBy: 'createdAt', sortDirection: 'desc' });
                            }
                        }}>
                            <Option value="newest">Newest</Option>
                            <Option value="oldest">Oldest</Option>
                            <Option value="priceAsc">Price Ascending</Option>
                            <Option value="priceDesc">Price Descending</Option>
                        </Select>
                    </div>
                </Col>
            </Row>
            <Row gutter={[24, 24]} className="product-list-grid">
                {isFetchingProduct ? (
                    <div className="flex justify-center w-100">
                        <Spin size="large" />
                    </div>
                ) : (products.map((product) => (
                    <Col key={product.id} xs={24} sm={12} md={10} lg={8} className="product-col">
                        <ProductItem product={product} />
                    </Col>
                )))}
            </Row>
        </div>
    );
};

export default ProductList;