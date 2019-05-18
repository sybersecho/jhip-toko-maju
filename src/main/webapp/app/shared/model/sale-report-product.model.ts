export interface ISaleReportProduct {
    barcode?: string;
    name?: string;
    unitName?: String;
    sellingPrice?: number;
    quantity?: number;
    totalPrice?: number;
    createdDate?: String;
}

export class SaleReportProduct implements ISaleReportProduct {
    constructor(
        public barcode?: string,
        public name?: string,
        public unitName?: String,
        public sellingPrice?: number,
        public quantity?: number,
        public totalPrice?: number,
        public createdDate?: String
    ) {}
}
