import {Layout, Spin} from "antd";
import Category from "./Category";
import {useState} from "react";
import {useGetAllCategoryQuery} from "../../redux/api/category-api";
import {useGetProductFilterQuery} from "../../redux/api/product-api";
import ProductList from "./ProductList";

const { Sider, Content } = Layout;

export const HomePage: React.FC = () => {
    const { data: categoriesData, isFetching: categoriesIsFetching } = useGetAllCategoryQuery();
    const [search, setSearch] = useState<string>('');
    const [selectedCategoryId, setSelectedCategoryId] = useState<string>("");
    const { data: productsData, isFetching: isFechingProduct } = useGetProductFilterQuery({ search, categoryId: selectedCategoryId, page: 0, limit: 20 });
    const handleCategorySelect = (categoryId: string) => {
        setSelectedCategoryId(categoryId);
    };

    return (
        <>
            {categoriesIsFetching || isFechingProduct ? (
            <div className="flex justify-center w-100">
                <Spin size="large" />
            </div>
            ) : (
            <Layout>
                <Sider width={260}>
                    <Category categories={categoriesData?.result ?? []} onCategorySelect={handleCategorySelect} />
                </Sider>
                <Layout className="bg-white">
                    <Content>
                        <ProductList products={productsData?.result?.content ?? []} />
                    </Content>
                </Layout>
            </Layout>
            )}
        </>
    );
};

export default HomePage;