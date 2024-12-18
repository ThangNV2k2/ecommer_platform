'use client';

import CategoryPopover from '@/app/dashboard/product/component/category';
import { CellActionProduct } from '@/app/dashboard/product/component/cell-action';
import CreateOrUpdateProduct from '@/app/dashboard/product/component/create-update-product';
import PromotionDetail from '@/app/dashboard/product/component/promotion';
import DebouncedInput from '@/components/debounce-input/debounce-input';
import PageContainer from '@/components/layout/page-container';
import { Spinner } from '@/components/spinner';
import { DataTable, DataTableColumnHeader } from '@/components/table/table-data';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import { Button } from '@/components/ui/button';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { formatDateString } from '@/constants/date';
import { getErrorMessage } from '@/constants/get-error';
import { useGetAllCategoriesQuery } from '@/redux/api/category-api';
import { useGetProductFilterQuery } from '@/redux/api/product-api';
import { useGetAllPromotionQuery } from '@/redux/api/promotion-api';
import { PaginationParams } from '@/types/page';
import { ProductResponse } from '@/types/product';
import { ColumnDef, ColumnSort } from '@tanstack/react-table';
import clsx from 'clsx';
import { AlertCircle } from 'lucide-react';
import { useMemo, useState } from 'react';

const ProductTable = () => {
    const [pagination, setPagination] = useState<PaginationParams>({
        page: 0,
        size: 10,
        sortBy: "name",
        sortDirection: "asc",
        search: "",
    });

    const { data: allProduct, isFetching, error, refetch } = useGetProductFilterQuery(pagination);
    const { data: allCategory } = useGetAllCategoriesQuery();
    const { data: allPromotion } = useGetAllPromotionQuery();
    const [messageError, setMessageError] = useState("");
    const [showCreateModal, setShowCreateModal] = useState(false);
    const columns = useMemo<ColumnDef<ProductResponse>[]>(() => [
        {
            accessorKey: "name",
            enableSorting: true,
            header: ({ column }) => <DataTableColumnHeader column={column} title="Name" />,
        },
        {
            accessorKey: "price",
            enableSorting: true,
            header: ({ column }) => <DataTableColumnHeader column={column} title="Price (đ)" />,
            cell: ({ row }) => <div>{row.original.price.toFixed(2)}</div>,
        },
        {
            accessorKey: "isActive",
            header: "Active",
            cell: ({ row }) => <div>{row.original.isActive ? "Active" : "Inactive"}</div>
        },
        {
            accessorKey: "categoryResponse.name",
            header: "Category",
            cell: ({ row }) => {
                const category = row.original.categoryResponse;
                return (
                    <Popover>
                        <PopoverTrigger asChild>
                            <Button variant="link">{category.name}</Button>
                        </PopoverTrigger>
                        <PopoverContent className="w-80 p-4 border rounded-md shadow-lg bg-white dark:bg-gray-800">
                            <div>
                                <CategoryPopover category={category} />
                            </div>
                        </PopoverContent>
                    </Popover>
                );
            }
        },
        {
            accessorKey: "promotions",
            header: "Promotions",
            cell: ({ row }) => {
                const promotions = row.original.promotions;
                const promotionApply = row.original.promotionResponse;
                return (
                    <div className="flex flex-col">
                        {promotions.map((promotion) => (
                            <Popover key={promotion.id}>
                                <PopoverTrigger asChild>
                                    <Button
                                        variant="link"
                                        className={clsx(
                                            promotion.id === promotionApply?.id ? "text-red-500 underline font-bold" : "text-primary"
                                        )}
                                    >
                                        {promotion.name}
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent>
                                    <PromotionDetail promotion={promotion} />
                                </PopoverContent>
                            </Popover>
                        ))}
                    </div>
                );
            }
        },
        {
            accessorKey: "rating",
            header: ({ column }) => <DataTableColumnHeader column={column} title="Rating" />,
            cell: ({ row }) => <div>{row.original.rating}</div>
        },
        {
            accessorKey: "description",
            header: "Description",
        },
        {
            accessorKey: "createdAt",
            enableSorting: true,
            header: ({ column }) => <DataTableColumnHeader column={column} title="Created" />,
            cell: ({ row }) => formatDateString(row.original.createdAt),
        },
        {
            id: 'actions',
            cell: ({ row }) => <CellActionProduct data={row.original} refetch={refetch} setError={setMessageError} categories={allCategory?.result ?? []} promotions={allPromotion?.result ?? []} />
        }
    ], [allCategory, allPromotion, refetch]);

    const handleSortingChange = (newSorting?: ColumnSort) => {
        if (newSorting) {
            setPagination((prev) => ({
                ...prev,
                sortBy: newSorting.id,
                sortDirection: newSorting.desc ? "desc" : "asc",
            }));
        } else {
            setPagination((prev) => ({
                ...prev,
                sortBy: "name",
                sortDirection: "asc",
            }));
        }
    };

    if (error) {
        return (
            <Alert variant="destructive">
                <AlertCircle className="h-4 w-4" />
                <AlertTitle>Error</AlertTitle>
                <AlertDescription>
                    {getErrorMessage(error)}
                </AlertDescription>
            </Alert>
        )
    }

    return (
        <PageContainer scrollable>
            {messageError && (
                <Alert variant="destructive" onClose={() => setMessageError("")}>
                    <AlertCircle className="h-4 w-4" />
                    <AlertTitle>Error</AlertTitle>
                    <AlertDescription>
                        {messageError}
                    </AlertDescription>
                </Alert>
            )}
            <CreateOrUpdateProduct
                isOpen={showCreateModal}
                onClose={() => setShowCreateModal(false)}
                setMessageError={setMessageError}
                refetch={refetch}
                categories={allCategory?.result ?? []}
                promotions={allPromotion?.result ?? []}
            />
            <div className="w-full px-4">
                <div className="flex items-center py-4 justify-between">
                    <DebouncedInput
                        placeholder="Filter name..."
                        value={pagination.search}
                        onChange={(value) =>
                            setPagination({
                                ...pagination,
                                search: value,
                            })
                        }
                        className="max-w-sm"
                    />
                    <Button size="lg" onClick={() => setShowCreateModal(true)}>
                        Add Product
                    </Button>
                </div>
                <div>
                    {isFetching ? <Spinner size="large" className="mt-10" /> : (
                        <DataTable
                            columns={columns}
                            data={allProduct?.result?.content ?? []}
                            size={pagination.size}
                            page={pagination.page}
                            setSize={(size) => setPagination((prev) => ({ ...prev, size }))}
                            setPage={(page) => setPagination((prev) => ({ ...prev, page }))}
                            total={allProduct?.result?.totalElements ?? 0}
                            onChangeSorting={handleSortingChange}
                        />
                    )}

                </div>
            </div>
        </PageContainer>
    );
};

export default ProductTable;
