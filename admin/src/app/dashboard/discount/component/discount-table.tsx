"use client";

import { useState, useMemo } from "react";
import { useGetDiscountSearchByCodeQuery } from "@/redux/api/discount-api";
import { Button } from "@/components/ui/button";
import { DataTable, DataTableColumnHeader } from "@/components/table/table-data";
import { Spinner } from "@/components/spinner";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { formatDateString } from "@/constants/date";
import { PaginationParams } from "@/types/page";
import { DiscountResponse } from "@/types/discount";
import { ColumnDef, ColumnSort } from "@tanstack/react-table";
import DebouncedInput from "@/components/debounce-input/debounce-input";
import { getErrorMessage } from "@/constants/get-error";
import { AlertCircle } from "lucide-react";
import CellActionDiscount from "@/app/dashboard/discount/component/cell-action";
import CreateOrUpdateDiscount from "@/app/dashboard/discount/component/create-update-discount";
import { DiscountTypeEnum } from "@/types/enums";

const DiscountTable = () => {
    const [pagination, setPagination] = useState<PaginationParams>({
        page: 0,
        size: 10,
        sortBy: "code",
        sortDirection: "asc",
        search: "",
    });

    const { data: allDiscounts, isFetching, error, refetch } = useGetDiscountSearchByCodeQuery(pagination);
    const [messageError, setMessageError] = useState("");
    const [showCreateModal, setShowCreateModal] = useState(false);

    const columns = useMemo<ColumnDef<DiscountResponse>[]>(() => [
        {
            accessorKey: "code",
            enableSorting: true,
            header: ({ column }) => <DataTableColumnHeader column={column} title="Code" />,
        },
        {
            accessorKey: "discountType",
            enableSorting: true,
            header: ({ column }) => <DataTableColumnHeader column={column} title="Discount Type" />,
        },
        {
            accessorKey: "discountPercentage",
            enableSorting: true,
            header: ({ column }) => <DataTableColumnHeader column={column} title="Discount (%)" />,
            cell: ({ row }) => row.original.discountType === DiscountTypeEnum.PERCENTAGE
                ? `${row.original.discountPercentage}%`
                : "",
        },
        {
            accessorKey: "discountValue",
            enableSorting: true,
            header: ({ column }) => <DataTableColumnHeader column={column} title="Discount(đ)" />,
            cell: ({ row }) => row.original.discountType === DiscountTypeEnum.VALUE
                ? row.original.discountValue
                : "",
        },
        {
            accessorKey: "expiryDate",
            enableSorting: true,
            header: ({ column }) => <DataTableColumnHeader column={column} title="Expiry Date" />,
            cell: ({ row }) => row.original.expiryDate ?  formatDateString(row.original.expiryDate) : "",
        },
        {
            accessorKey: "minOrderValue",
            header: "Min Order Value",
        },
        {
            accessorKey: "maxDiscountValue",
            header: "Max Discount Value",
        },
        {
            accessorKey: "maxUses",
            header: "max Users"
        },
        {
            accessorKey: "usedCount",
            header: "Used Count"
        },
        {
            id: 'actions',
            cell: ({ row }) => <CellActionDiscount data={row.original} refetch={refetch} setError={setMessageError} />
        },
    ], []);

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
                sortBy: "code",
                sortDirection: "asc",
            }));
        }
    };

    if (error) {
        return (
            <Alert variant="destructive">
                <AlertCircle className="h-4 w-4" />
                <AlertTitle>Error</AlertTitle>
                <AlertDescription>{getErrorMessage(error)}</AlertDescription>
            </Alert>
        );
    }

    return (
        <div>
            {messageError && (
                <Alert variant="destructive" onClose={() => setMessageError("")}>
                    <AlertCircle className="h-4 w-4" />
                    <AlertTitle>Error</AlertTitle>
                    <AlertDescription>{messageError}</AlertDescription>
                </Alert>
            )}
            <CreateOrUpdateDiscount
                isOpen={showCreateModal}
                onClose={() => setShowCreateModal(false)}
                setMessageError={setMessageError}
                refetch={refetch}
            />
            <div className="w-full px-4">
                <div className="flex items-center py-4 justify-between">
                    <DebouncedInput
                        placeholder="Filter by code..."
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
                        Add Discount
                    </Button>
                </div>
                <div>
                    {isFetching ? (
                        <Spinner size="large" className="mt-10" />
                    ) : (
                        <DataTable
                            columns={columns}
                            data={allDiscounts?.result?.content ?? []}
                            size={pagination.size}
                            page={pagination.page}
                            setSize={(size) => setPagination((prev) => ({ ...prev, size }))}
                            setPage={(page) => setPagination((prev) => ({ ...prev, page }))}
                            total={allDiscounts?.result?.totalElements ?? 0}
                            onChangeSorting={handleSortingChange}
                        />
                    )}
                </div>
            </div>
        </div>
    );
};

export default DiscountTable;
