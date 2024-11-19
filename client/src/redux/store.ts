import {configureStore} from "@reduxjs/toolkit"
import {authApi} from "./api/auth-api"
import {setupListeners} from "@reduxjs/toolkit/query"
import {userApi} from "./api/user-api";
import userReducer from './slice/userSlice';
import cartReducer from './slice/cartSlice';
import {categoryApi} from "./api/category-api";
import {productApi} from "./api/product-api";
import {invalidTokenMiddleware} from "./api/invalid-token-middleware";
import {productInventoryApi} from "./api/product-inventory";
import {cartApi} from "./api/cart";
import {discountApi} from "./api/discount";
import {orderApi} from "./api/order";
import {shippingAddressApi} from "./api/shipping-address";
import {invoiceApi} from "./api/invoice";

export const store = configureStore({
    reducer: {
        [authApi.reducerPath]: authApi.reducer,
        [userApi.reducerPath]: userApi.reducer,
        [categoryApi.reducerPath]: categoryApi.reducer,
        [productApi.reducerPath]: productApi.reducer,
        [productInventoryApi.reducerPath]: productInventoryApi.reducer,
        [cartApi.reducerPath]: cartApi.reducer,
        [discountApi.reducerPath]: discountApi.reducer,
        [orderApi.reducerPath]: orderApi.reducer,
        [shippingAddressApi.reducerPath]: shippingAddressApi.reducer,
        [invoiceApi.reducerPath]: invoiceApi.reducer,
        user: userReducer,
        cart: cartReducer,
    },

    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat(authApi.middleware, userApi.middleware, categoryApi.middleware,
            productApi.middleware, productInventoryApi.middleware, cartApi.middleware,
            discountApi.middleware, shippingAddressApi.middleware, invoiceApi.middleware, orderApi.middleware
        ).concat(invalidTokenMiddleware),
})

export type RootState = ReturnType<typeof store.getState>;
setupListeners(store.dispatch)