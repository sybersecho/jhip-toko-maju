import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { ISupplier } from 'app/shared/model/supplier.model';

export interface IProduct {
    id?: number;
    barcode?: string;
    name?: string;
    unit?: string;
    warehousePrices?: number;
    unitPrices?: number;
    sellingPrices?: number;
    stock?: number;
    customerProduct?: ICustomerProduct;
    products?: ISupplier[];
}

export class Product implements IProduct {
    constructor(
        public id?: number,
        public barcode?: string,
        public name?: string,
        public unit?: string,
        public warehousePrices?: number,
        public unitPrices?: number,
        public sellingPrices?: number,
        public stock?: number,
        public customerProduct?: ICustomerProduct,
        public products?: ISupplier[]
    ) {}
}
