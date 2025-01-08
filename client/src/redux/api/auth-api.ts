import {createApi, fetchBaseQuery} from "@reduxjs/toolkit/query/react";
import {BaseResponse} from "../../types/base-response";
import {LoginEmailRequest, LoginResponse} from "../../types/login";
import {ChangePasswordRequest, UserInfo} from "../../types/user-info";
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

        verifyEmail: builder.mutation<BaseResponse<string>, string>({
            query: (token) => ({
                url: `auth/verify?token=${token}`,
                method: "GET",
            }),
        }),

        sendOtp: builder.query<BaseResponse<string>, string>({
            query: (email) => ({
                url: `auth/otp?email=${email}`,
                method: "GET",
            }),
        }),

        changePassword: builder.mutation<BaseResponse<string>, ChangePasswordRequest>({
            query: (request) => ({
                url: "auth/change-password",
                method: "POST",
                body: request
            }),
        }),
    })
});

export const {useLoginEmailMutation, useRegisterMutation, useVerifyEmailMutation, useLazySendOtpQuery, useChangePasswordMutation} = authApi;