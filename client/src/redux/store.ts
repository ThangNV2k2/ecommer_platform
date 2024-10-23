import { configureStore } from "@reduxjs/toolkit"
import { authApi } from "./api/auth-api"
import { setupListeners } from "@reduxjs/toolkit/query"
import {userApi} from "./api/user-api";
import userReducer from './slice/userSlice';
import {categoryApi} from "./api/category-api";
import {productApi} from "./api/product-api";


export const store = configureStore({
    reducer: {
        [authApi.reducerPath]: authApi.reducer,
        [userApi.reducerPath]: userApi.reducer,
        [categoryApi.reducerPath]: categoryApi.reducer,
        [productApi.reducerPath]: productApi.reducer,
        user: userReducer,
    },

    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat(authApi.middleware, userApi.middleware, categoryApi.middleware, productApi.middleware),
})

export type RootState = ReturnType<typeof store.getState>;
setupListeners(store.dispatch)