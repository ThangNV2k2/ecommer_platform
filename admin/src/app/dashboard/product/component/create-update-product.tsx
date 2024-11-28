'use client';

import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useCreateProductMutation, useUpdateProductMutation } from "@/redux/api/product-api";
import { CategoryResponse } from "@/types/category";
import { ProductResponse } from "@/types/product";
import { PromotionResponse } from "@/types/promotion";
import { Controller, useForm } from "react-hook-form";
import CategorySelect from "@/app/dashboard/product/component/category-select";
import Image from "next/image";
import { useUploadImageMutation } from "@/redux/api/upload-image";
import { useState } from "react";
import PromotionsSelect from "@/app/dashboard/product/component/promotion-select";

interface CreateOrUpdateProductProps {
    isOpen: boolean;
    onClose: () => void;
    product?: ProductResponse;
    setMessageError: (message: string) => void;
    refetch: () => void;
    categories: CategoryResponse[];
    promotions: PromotionResponse[];
}

const CreateOrUpdateProduct = ({ isOpen, onClose, product, setMessageError, refetch, categories, promotions }: CreateOrUpdateProductProps) => {
    const { register, handleSubmit, reset, formState: { errors }, control, setValue } = useForm({
        defaultValues: {
            name: product?.name ?? "",
            description: product?.description ?? "",
            price: product?.price ?? 0,
            categoryId: product?.categoryResponse?.id ?? "",
            isActive: product?.isActive ?? true,
            promotionIds: product?.promotions.map(promotion => promotion.id) ?? [],
            mainImage: product?.mainImage ?? "",
        },
    });

    const [createProduct, { isLoading: isCreating }] = useCreateProductMutation();
    const [updateProduct, { isLoading: isUpdating }] = useUpdateProductMutation();
    const [uploadImage, { isLoading: isUploading }] = useUploadImageMutation();
    const [imagePreview, setImagePreview] = useState<string | null>(
        product?.mainImage ? product.mainImage : null
    );

    const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0];
        if (file) {
            const formData = new FormData();
            formData.append("file", file);
            uploadImage(formData)
                .unwrap()
                .then((response) => {
                    if (response.result) {
                        setValue("mainImage", response.result);
                        setImagePreview(response.result);
                    }
                })
                .catch((error) => {
                    setMessageError("Error uploading image: " + error.message);
                });
        }
    };

    const handleRemoveImage = () => {
        setValue("mainImage", "");
        setImagePreview(null);
    };

    const onSubmit = async (data: any) => {
        try {
            if (product) {
                await updateProduct({ id: product.id, product: data }).unwrap();
            } else {
                await createProduct(data).unwrap();
            }
            refetch();
            handleClose();
        } catch (error: any) {
            setMessageError(data.name + ": " + (error.data?.message ?? "An error occurred"));
            handleClose();
        }
    };

    const handleClose = () => {
        reset();
        onClose();
    }

    return (
        <Dialog open={isOpen} onOpenChange={handleClose} modal>
            <DialogContent className="sm:max-w-[500px]">
                <DialogHeader>
                    <DialogTitle>{product ? "Edit Product" : "Add Product"}</DialogTitle>
                    <DialogDescription>
                        {product ? "Update the product details below." : "Fill in the details to create a new product."}
                    </DialogDescription>
                </DialogHeader>
                <form onSubmit={handleSubmit(onSubmit)} className="grid gap-4 py-4">
                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="name" className="text-right">
                            Name <span className="text-red-500">*</span>
                        </Label>
                        <Input
                            id="name"
                            {...register("name", { required: "Name is required" })}
                            className={`col-span-3 ${errors.name ? 'border-red-500' : ''}`}
                        />
                        {errors.name && <p className="col-span-4 text-red-500 text-sm">{errors.name.message}</p>}
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="description" className="text-right">
                            Description
                        </Label>
                        <Input
                            id="description"
                            {...register("description")}
                            className="col-span-3"
                        />
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="price" className="text-right">
                            Price <span className="text-red-500">*</span>
                        </Label>
                        <Input
                            type="number"
                            min={0}
                            id="price"
                            {...register("price", { required: "Price is required" })}
                            className={`col-span-3 ${errors.price ? 'border-red-500' : ''}`}
                        />
                        {errors.price && <p className="col-span-4 text-red-500 text-sm">{errors.price.message}</p>}
                    </div>
                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="categoryId" className="text-right">
                            Category <span className="text-red-500">*</span>
                        </Label>
                        <Controller
                            name="categoryId"
                            control={control}
                            rules={{ required: "Category is required" }}
                            render={({ field: { value, onChange } }) => (
                                <CategorySelect
                                    value={value}
                                    onChange={onChange}
                                    categories={categories}
                                />
                            )}
                        />
                        {errors.categoryId && (
                            <p className="col-span-4 text-red-500 text-sm">{errors.categoryId.message}</p>
                        )}
                    </div>

                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="promotionIds" className="text-right">
                            Promotions <span className="text-red-500"></span>
                        </Label>
                        <Controller
                            name="promotionIds"
                            control={control}
                            render={({ field: { value, onChange } }) => (
                                <PromotionsSelect
                                    value={value}
                                    onChange={onChange}
                                    promotions={promotions}
                                />
                            )}
                        />
                        
                    </div>

                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="mainImage" className="text-right">
                            Image <span className="text-red-500">*</span>
                        </Label>
                        <div className="col-span-3">
                            {imagePreview ? (
                                <div className="relative mb-2">
                                    <Image src={imagePreview} alt="Preview" width={80} height={80} className="object-cover" />
                                    <Button
                                        variant="outline"
                                        size="icon"
                                        className="absolute top-0 right-0"
                                        onClick={handleRemoveImage}
                                    >
                                        X
                                    </Button>
                                </div>
                            ) : (
                                <Input
                                    id="mainImage"
                                    type="file"
                                    accept="image/*"
                                    onChange={handleImageChange}
                                    disabled={isUploading}
                                />
                            )}
                        </div>
                    </div>

                    <DialogFooter>
                        <Button variant="outline" onClick={handleClose} disabled={isCreating || isUpdating}>
                            Cancel
                        </Button>
                        <Button type="submit" disabled={isCreating || isUpdating}>
                            {product ? (isUpdating ? "Saving..." : "Save Changes") : (isCreating ? "Adding..." : "Add Product")}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
};

export default CreateOrUpdateProduct;