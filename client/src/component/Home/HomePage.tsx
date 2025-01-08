import { Layout, Spin } from "antd";
import Category from "./Category";
import { useEffect, useState } from "react";
import { useGetAllCategoryQuery } from "../../redux/api/category-api";
import { useGetProductFilterQuery } from "../../redux/api/product-api";
import ProductList from "./ProductList";
import '../../sass/home-page.scss';
import { useSearchParams } from "react-router-dom";
import { SortType } from "../../types/page";
import { ProductResponseKeys } from "../../types/product";

const { Sider, Content } = Layout;

export const HomePage: React.FC = () => {
    const { data: categoriesData, isFetching: categoriesIsFetching } = useGetAllCategoryQuery();
    const [searchParams, setSearchParams] = useSearchParams();
    const search = searchParams.get('search') || '';
    const categoryId = searchParams.get('categoryId') || '';
    const [sortProduct, setSortProduct] = useState<{ sortBy: ProductResponseKeys, sortDirection: SortType }>({ sortBy: "createdAt", sortDirection: 'asc' });
    const { data: productsData, isFetching: isFetchingProduct } = useGetProductFilterQuery({ search, categoryId: categoryId, page: 0, size: 30, sortBy: sortProduct.sortBy, sortDirection: sortProduct.sortDirection });

    const categoryName = categoriesData?.result?.find((category) => category.id === categoryId)?.name ?? "All Category";

    return (
        <>
            {categoriesIsFetching ? (
                <div className="flex justify-center w-100">
                    <Spin size="large" />
                </div>
            ) : (
                <Layout className="responsive-layout">
                    <Sider breakpoint="lg" collapsedWidth="0" className="responsive-sider">
                        <Category categories={categoriesData?.result ?? []} />
                    </Sider>
                    <Layout className="bg-white responsive-content">
                        <Content className="px-4">
                            <ProductList products={productsData?.result?.content ?? []} categoryName={categoryName} setSortProduct={setSortProduct} isFetchingProduct={isFetchingProduct} />
                        </Content>
                    </Layout>
                </Layout>
            )}
        </>
    );
};

export default HomePage;