import {CategoryResponse} from "./category";

export interface ProductResponse {
    id: string;
    name: string;
    description: string;
    price: number;
    category: CategoryResponse;
    rating: number;
    isActive: boolean;
    discountPercentage: number;
    mainImage: string;
}
