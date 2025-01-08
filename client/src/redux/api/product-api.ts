import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {baseApi} from "./auth-api";
import {BaseResponse} from "../../types/base-response";
import {ProductResponse, ProductResponseKeys} from "../../types/product";
import {PageResponse, SortType} from "../../types/page";

interface ProductFilterRequest {
    search?: string,
    categoryId?: string,
    page?: number,
    size?: number,
    sortBy?: ProductResponseKeys;
    sortDirection?: SortType;
}

export const productApi = createApi({
    reducerPath: "productApi",
    baseQuery: fetchBaseQuery({baseUrl: baseApi}),
    endpoints: (builder) => ({
        getProductFilter: builder.query<BaseResponse<PageResponse<ProductResponse>>, ProductFilterRequest>({
            query: (productFilter) => {
                const {search, categoryId, page, size, sortBy, sortDirection} = productFilter;
                let url = `product?page=${page}&size=${size}&sort=${sortBy},${sortDirection}&name=${search}`;
                if (categoryId) {
                    url += `&categoryId=${categoryId}`;
                }
                return {
                    url,
                    method: "GET"
                };
            },

            serializeQueryArgs: ({ queryArgs }) => {
                const { search, categoryId, sortBy, sortDirection } = queryArgs;
                return `${search}-${categoryId}-${sortBy}-${sortDirection}`;
            },
            merge: (currentCache: BaseResponse<PageResponse<ProductResponse>>, newData: BaseResponse<PageResponse<ProductResponse>>) => {
                if (newData.result && currentCache.result) {
                    currentCache.result.content.push(...(newData.result.content || []));
                    currentCache.result.totalElements = newData.result.totalElements || currentCache.result.totalElements;
                    currentCache.result.totalPages = newData.result.totalPages || currentCache.result.totalPages;
                }
            },
            forceRefetch: ({ currentArg, previousArg }) => {
                return (
                    (currentArg?.search !== previousArg?.search) ||
                    (currentArg?.categoryId !== previousArg?.categoryId) ||
                    (currentArg?.sortBy !== previousArg?.sortBy) ||
                    (currentArg?.sortDirection !== previousArg?.sortDirection)
                );
            }
        }),
        getProductById: builder.query<BaseResponse<ProductResponse>, string>({
            query: (id) => ({
                url: `product/${id}`,
                method: "GET",
            })
        }),
    }),
});

export const {useGetProductFilterQuery, useLazyGetProductFilterQuery, useGetProductByIdQuery} = productApi;