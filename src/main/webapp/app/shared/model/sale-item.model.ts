import { UnitMeasure, IProduct, Product } from './product.model';

export interface ISaleItem {
    id?: number;
    quantity?: number;
    totalPrice?: number;
    sellingPrice?: number;
    productName?: string;
    unit?: UnitMeasure;
    productId?: number;
    saleNoInvoice?: string;
    saleId?: number;
    product?: IProduct;
}

export class SaleItem implements ISaleItem {
    constructor(
        public id?: number,
        public quantity?: number,
        public totalPrice?: number,
        public productName?: string,
        public productId?: number,
        public saleNoInvoice?: string,
        public saleId?: number,
        public unit?: UnitMeasure,
        public sellingPrice?: number,
        public product?: IProduct
    ) {
        // this.product = new Product();
    }
}
