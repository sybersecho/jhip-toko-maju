import { IProduct, Product } from './product.model';
import { IUnit } from './unit.model';

export interface ISaleItem {
    id?: number;
    quantity?: number;
    totalPrice?: number;
    sellingPrice?: number;
    productName?: string;
    unit?: String;
    productId?: number;
    saleNoInvoice?: string;
    saleId?: number;
    barcode?: string;
    product?: IProduct;

    createItem(): SaleItem;
    associateProduct(): void;
    isQtyBigerThanStock(): boolean;
    setProduct(product: IProduct): void;
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
        public unit?: String,
        public sellingPrice?: number,
        public barcode?: string,
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

    setProduct(product: IProduct): void {
        this.product = product;
        this.productName = product.name;
        this.productId = product.id;
        this.sellingPrice = product.sellingPrice;
        this.unit = product.unitName;
        this.barcode = product.barcode;
    }

    protected calculateTotalPrice(): void {
        this.totalPrice = this.sellingPrice * this.quantity;
    }
}
