import { LoyaltyTierEnum, RoleEnum } from "./enums";

export interface PhoneNumber {
    id: string;
    phoneNumber: string;
}

export interface Address {
    id: string;
    addressDetail: string;
    country: string;
}

export interface UserInfo {
    id: string;
    email: string;
    name: string;
    isActive: boolean;
    loyaltyTier: LoyaltyTierEnum;
    roles: Set<RoleEnum>;
    createdAt: Date;
    updatedAt: Date;
    phoneNumbers?: PhoneNumber[];
    addresses?: Address[];
}

export interface User {
    id: string;
    email: string;
    name: string;
    isActive: boolean;
    loyaltyTier: LoyaltyTierEnum;
    roles: Set<RoleEnum>;
    createdAt: Date;
    updatedAt: Date;
    phoneNumbers?: PhoneNumber[];
    addresses?: Address[];
}