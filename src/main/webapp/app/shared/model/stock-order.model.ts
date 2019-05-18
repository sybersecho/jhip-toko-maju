export interface IStockOrder {
    barcode?: string;
    name?: string;
    unitName?: string;
    unitPrice?: number;
    quantity?: number;
    totalPrice?: number;
}

export class StockOrder implements IStockOrder {
    constructor(
        public barcode?: string,
        public name?: string,
        public unitName?: string,
        public unitPrice?: number,
        public quantity?: number,
        public totalPrice?: number
    ) {}
}
