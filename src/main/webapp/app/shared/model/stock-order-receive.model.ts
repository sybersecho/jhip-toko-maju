import { Moment } from 'moment';
export interface IStockOrderReceive {
    barcode?: string;
    name?: string;
    quantity?: number;
    createdDate?: Moment;
}

export class StockOrderReceive implements IStockOrderReceive {
    constructor(public barcode?: string, public name?: string, public quantity?: number, public createdDate?: Moment) {}
}
