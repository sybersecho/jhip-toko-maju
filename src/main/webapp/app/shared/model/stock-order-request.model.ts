import { Moment } from 'moment';

export interface IStockOrderRequest {
    barcode?: string;
    name?: string;
    unitName?: string;
    unitPrice?: number;
    quantity?: number;
    totalPrice?: number;
}

export class StockOrderRequest implements IStockOrderRequest {
    constructor(
        public barcode?: string,
        public name?: string,
        public unitName?: string,
        public unitPrice?: number,
        public quantity?: number,
        public totalPrice?: number
    ) {}

    calculateTotal() {
        this.totalPrice = this.quantity * this.unitPrice;
    }
}
