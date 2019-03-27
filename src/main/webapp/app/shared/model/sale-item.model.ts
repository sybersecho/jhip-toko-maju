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

    createItem(): SaleItem;
    associateProduct(): void;
    isQtyBigerThanStock(): boolean;
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
    ) {}

    public createItem(): SaleItem {
        this.calculateTotalPrice();
        this.associateProduct();
        return this;
    }

    public associateProduct(): void {
        this.productId = this.product.id;
        this.productName = this.product.name;
    }

    public isQtyBigerThanStock(): boolean {
        if (this.product) {
            return this.quantity > this.product.stock;
        }
        return false;
    }

    protected calculateTotalPrice(): void {
        this.totalPrice = this.product.sellingPrice * this.quantity;
    }
}
