export interface ICustomerProduct {
    id?: number;
    specialPrice?: number;
    productName?: string;
    productId?: number;
    customerCode?: string;
    customerId?: number;
    barcode?: String;
    unitPrice?: number;
    sellingPrice?: number;
    unit?: String;
}

export class CustomerProduct implements ICustomerProduct {
    constructor(
        public id?: number,
        public specialPrice?: number,
        public productName?: string,
        public productId?: number,
        public customerCode?: string,
        public customerId?: number,
        public barcode?: String,
        public unitPrice?: number,
        public sellingPrice?: number,
        public unit?: String
    ) {}
}
