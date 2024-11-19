import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {BaseResponse} from "../../types/base-response";
import {LoginEmailRequest, LoginResponse} from "../../types/login";
import {UserInfo} from "../../types/user-info";
import {RegisterRequest} from "../../types/register";

export const baseApi = process.env.REACT_APP_BASE_API_URL;

export const authApi = createApi({
    reducerPath: "authApi",
    baseQuery: fetchBaseQuery({baseUrl: baseApi}),
    endpoints: (builder) => ({
        loginEmail: builder.mutation<BaseResponse<LoginResponse>, LoginEmailRequest>({
            query: (user) => ({
                url: "auth/login",
                method: "POST",
                body: user,
            }),
        }),
        register: builder.mutation<BaseResponse<UserInfo>, RegisterRequest>({
            query: (user) => ({
                url: "auth/register",
                method: "POST",
                body: user,
            }),
        }),
    }),
});

export const {useLoginEmailMutation, useRegisterMutation} = authApi;