export const enum ProductStatus {
    GOOD = 'GOOD',
    BAD = 'BAD'
}

export interface IReturnItem {
    id?: number;
    barcode?: string;
    productName?: string;
    quantity?: number;
    unitPrice?: number;
    productStatus?: ProductStatus;
    productBarcode?: string;
    productId?: number;
    returnTransactionId?: number;
}

export class ReturnItem implements IReturnItem {
    constructor(
        public id?: number,
        public barcode?: string,
        public productName?: string,
        public quantity?: number,
        public unitPrice?: number,
        public productStatus?: ProductStatus,
        public productBarcode?: string,
        public productId?: number,
        public returnTransactionId?: number
    ) {}
}
