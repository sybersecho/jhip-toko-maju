export interface IStockOrderReceive {
    barcode?: string;
    name?: string;
    quantity?: number;
}

export class StockOrderReceive implements IStockOrderReceive {
    constructor(public barcode?: string, public name?: string, public quantity?: number) {}
}
