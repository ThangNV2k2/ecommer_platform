import { configureStore } from "@reduxjs/toolkit"
import { authApi } from "./api/auth-api"
import { setupListeners } from "@reduxjs/toolkit/query"
import {userApi} from "./api/user-api";
import userReducer from './slice/userSlice';
import {categoryApi} from "./api/category-api";
import {productApi} from "./api/product-api";
import { promotionApi } from "@/redux/api/promotion-api";
import { imageApi } from "@/redux/api/upload-image";
import { discountApi } from "@/redux/api/discount-api";
import { orderApi } from "@/redux/api/order-api";
import { shippingAddressApi } from "@/redux/api/shipping-address";
import { invoiceApi } from "@/redux/api/invoice-api";
import { chatApi } from "@/redux/api/chat-api";


export const store = configureStore({
    reducer: {
        [authApi.reducerPath]: authApi.reducer,
        [userApi.reducerPath]: userApi.reducer,
        [categoryApi.reducerPath]: categoryApi.reducer,
        [productApi.reducerPath]: productApi.reducer,
        [promotionApi.reducerPath]: promotionApi.reducer,
        [imageApi.reducerPath]: imageApi.reducer,
        [discountApi.reducerPath]: discountApi.reducer,
        [orderApi.reducerPath]: orderApi.reducer,
        [shippingAddressApi.reducerPath]: shippingAddressApi.reducer,
        [invoiceApi.reducerPath]: invoiceApi.reducer,
        [chatApi.reducerPath]: chatApi.reducer,
        user: userReducer,
    },

    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat(authApi.middleware, userApi.middleware, categoryApi.middleware, productApi.middleware,
            promotionApi.middleware, imageApi.middleware, discountApi.middleware, orderApi.middleware, shippingAddressApi.middleware,
            invoiceApi.middleware, chatApi.middleware),
})

export type RootState = ReturnType<typeof store.getState>;
setupListeners(store.dispatch)