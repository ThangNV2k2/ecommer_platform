export interface CategoryRequest {
    name: string;
    description: string;
    isActive: boolean;
}

export interface CategoryResponse {
    id: string;
    name: string;
    description: string;
    isActive: boolean;
    createdAt: Date;
    updatedAt: Date;
}
