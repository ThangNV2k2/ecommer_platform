import { PromotionResponse } from "@/types/promotion";
import {CategoryResponse} from "./category";

export interface ProductResponse {
    id: string;
    name: string;
    description: string;
    price: number;
    promotionResponse: PromotionResponse | null;
    categoryResponse: CategoryResponse;
    promotions: PromotionResponse[];
    rating: number;
    isActive: boolean;
    discountPercentage: number;
    mainImage: string;
    createdAt: Date;
    updatedAt: Date;
}

export interface ProductRequest {
    name: string;
    description?: string;
    price: number;
    categoryId: string;
    isActive: boolean;
    promotionIds?: string[];
    mainImage?: string;
}
