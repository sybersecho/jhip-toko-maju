export interface ICustomerProduct {
    id?: number;
    specialPrice?: number;
    productName?: string;
    productId?: number;
    customerCode?: string;
    customerId?: number;
    barcode?: String;
    unitPrices?: number;
    sellingPrices?: number;
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
        public unitPrices?: number,
        public sellingPrices?: number
    ) {}
}
