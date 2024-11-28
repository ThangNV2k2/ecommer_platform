"use client";

import { useState } from "react";
import { Trash, Edit, MoreHorizontal } from "lucide-react";
import { Button } from "@/components/ui/button";
import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuItem, DropdownMenuLabel } from "@/components/ui/dropdown-menu";
import { useDeleteDiscountMutation } from "@/redux/api/discount-api";
import { AlertModal } from "@/components/modal/alert-modal";
import { DiscountResponse } from "@/types/discount";
import CreateOrUpdateDiscount from "@/app/dashboard/discount/component/create-update-discount";

interface CellActionDiscountProps {
    data: DiscountResponse;
    refetch: () => void;
    setError: (message: string) => void;
}

const CellActionDiscount = ({ data, refetch, setError }: CellActionDiscountProps) => {
    const [deleteDiscount, { isLoading }] = useDeleteDiscountMutation();
    const [showUpdateModal, setShowUpdateModal] = useState(false);
    const [open, setOpen] = useState(false);

    const handleDelete = () => {
        deleteDiscount(data.id).unwrap()
            .then(() => {
                setOpen(false);
                refetch();
            }).catch((error) => {
                setError(error.data.message);
                setOpen(false);
            });
    };

    return (
        <>
            <DropdownMenu modal={false}>
                <DropdownMenuTrigger asChild>
                    <Button variant="ghost" className="h-8 w-8 p-0">
                        <span className="sr-only">Open menu</span>
                        <MoreHorizontal className="h-4 w-4" />
                    </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                    <DropdownMenuLabel>Actions</DropdownMenuLabel>

                    <DropdownMenuItem
                        onClick={() => setShowUpdateModal(true)}
                    >
                        <Edit className="mr-2 h-4 w-4" /> Update
                    </DropdownMenuItem>
                    <DropdownMenuItem onClick={() => setOpen(true)}>
                        <Trash className="mr-2 h-4 w-4" /> Delete
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu>

            <AlertModal
                isOpen={open}
                onClose={() => setOpen(false)}
                loading={isLoading}
                onConfirm={handleDelete}
                title="Confirm Delete"
                description="Are you sure you want to delete this discount?"
            />

            <CreateOrUpdateDiscount
                isOpen={showUpdateModal}
                onClose={() => setShowUpdateModal(false)}
                refetch={refetch}
                setMessageError={setError}
                discount={data}
            />
        </>
    );
};

export default CellActionDiscount;