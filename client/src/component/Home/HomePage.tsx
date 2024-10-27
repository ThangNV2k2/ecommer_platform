import {Layout} from "antd";
import Category from "./Category";
import Product from "./ProductList";
import {useState} from "react";
import {useGetAllCategoryQuery} from "../../redux/api/category-api";
import {useGetProductFilterQuery} from "../../redux/api/product-api";
import ProductList from "./ProductList";

const { Sider, Content } = Layout;

export const HomePage: React.FC = () => {
    const { data: categoriesData, isSuccess, isLoading } = useGetAllCategoryQuery();
    const [search, setSearch] = useState<string>('');
    const [selectedCategoryId, setSelectedCategoryId] = useState<string>("");
    const { data: productsData } = useGetProductFilterQuery({ search, categoryId: selectedCategoryId, page: 0, limit: 20 });
    const handleCategorySelect = (categoryId: string) => {
        setSelectedCategoryId(categoryId);
    };

    return (
        <Layout>
            <Sider width={200}>
                <Category categories={categoriesData?.result ?? []} onCategorySelect={handleCategorySelect} />
            </Sider>
            <Layout className="bg-white">
                <Content>
                    <ProductList products={productsData?.result?.content ?? []} />
                </Content>
            </Layout>
        </Layout>
    );
};

export default HomePage;