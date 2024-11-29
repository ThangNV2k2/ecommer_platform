'use client';
import CreateOrUpdateCategory from '@/app/dashboard/category/_component/create-update-category';
import { AlertModal } from '@/components/modal/alert-modal';
import { Button } from '@/components/ui/button';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuTrigger
} from '@/components/ui/dropdown-menu';
import { useDeleteCategoryMutation } from '@/redux/api/category-api';
import { CategoryResponse } from '@/types/category';
import { Edit, MoreHorizontal, Trash } from 'lucide-react';
import { useState } from 'react';

interface CellActionProps {
    data: CategoryResponse;
    refetch: () => void;
    setError: (message: string) => void;
}

export const CellAction: React.FC<CellActionProps> = ({ data, refetch, setError }) => {
    const [deleteCategory, { isLoading: isDeleting }] = useDeleteCategoryMutation();
    const [showUpdateModal, setShowUpdateModal] = useState(false);
    const [open, setOpen] = useState(false);

    const onConfirm = () => {
        deleteCategory(data.id).unwrap()
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
            <AlertModal
                isOpen={open}
                onClose={() => setOpen(false)}
                onConfirm={onConfirm}
                loading={isDeleting}
                title={`Delete ${data.name}`}
                description='Are you sure you want to delete this category?'
            />
            <CreateOrUpdateCategory 
                isOpen={showUpdateModal}
                onClose={() => setShowUpdateModal(false)}
                category={data}
                setMessageError={setError}
                refetch={refetch}
            />
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
        </>
    );
};
