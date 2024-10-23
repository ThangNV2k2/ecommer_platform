export interface BaseResponse<T> {
    code: number;
    message?: String;
    result?: T;
}

