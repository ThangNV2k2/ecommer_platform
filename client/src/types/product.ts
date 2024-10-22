import {CategoryResponse} from "./category";

export interface ProductRequest {
    name: string;
    description: string;
    price: number;
    categoryId: string;
    isActive: boolean;
    mainImage: string;
}

export interface ProductResponse {
    id: string;
    name: string;
    description: string;
    price: number;
    category: CategoryResponse;
    rating: number;
    mainImage: string;
    createdAt: Date;
    updatedAt: Date;
}