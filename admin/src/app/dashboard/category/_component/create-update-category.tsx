"use client";

import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useCreateCategoryMutation, useUpdateCategoryMutation } from "@/redux/api/category-api";
import { CategoryResponse } from "@/types/category";
import { useEffect } from "react";
import { Controller, useForm } from "react-hook-form";

interface CreateOrUpdateCategoryProps {
    isOpen: boolean;
    onClose: () => void;
    category?: CategoryResponse;
    setMessageError: (message: string) => void;
    refetch: () => void;
}

const CreateOrUpdateCategory = ({ isOpen, onClose, category, setMessageError, refetch }: CreateOrUpdateCategoryProps) => {
    const { register, handleSubmit, reset, formState: { errors }, control } = useForm({
        defaultValues: {
            name: category?.name ?? "",
            description: category?.description ?? "",
            isActive: category?.isActive ?? true,
        },
    });

    const [createCategory, { isLoading: isCreating }] = useCreateCategoryMutation();
    const [updateCategory, { isLoading: isUpdating }] = useUpdateCategoryMutation();

    useEffect(() => {
        if (category) {
            reset({
                name: category.name,
                description: category.description,
                isActive: category.isActive,
            });
        } else {
            reset({
                name: "",
                description: "",
                isActive: true,
            });
        }
    }, [category, reset]);

    const onSubmit = async (data: any) => {
        try {
            if (category) {
                await updateCategory({ id: category.id, category: data }).unwrap();
            } else {
                await createCategory(data).unwrap();
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
        <Dialog open={isOpen} onOpenChange={handleClose}>
            <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                    <DialogTitle>{category ? "Edit Category" : "Add Category"}</DialogTitle>
                    <DialogDescription>
                        {category ? "Update the category details below." : "Fill in the details to create a new category."}
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
                        <Label htmlFor="isActive" className="text-right">
                            Active
                        </Label>
                        <Controller
                            name="isActive"
                            control={control}
                            render={({ field }) => (
                                <Checkbox
                                    id="isActive"
                                    checked={field.value}
                                    onCheckedChange={field.onChange}
                                    className="mr-2"
                                />
                            )}
                        />
                    </div>
                    <DialogFooter>
                        <Button variant="outline" onClick={handleClose} disabled={isCreating || isUpdating}>
                            Cancel
                        </Button>
                        <Button type="submit" disabled={isCreating || isUpdating}>
                            {category ? (isUpdating ? "Saving..." : "Save Changes") : (isCreating ? "Adding..." : "Add Category")}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
};


export default CreateOrUpdateCategory;