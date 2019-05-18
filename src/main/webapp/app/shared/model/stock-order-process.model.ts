import { Moment } from 'moment';
export interface IStockOrderProcess {
    barcode?: string;
    name?: string;
    quantityRequest?: number;
    stockInHand?: number;
    quantityApprove?: number;
    createdDate?: Moment;
}

export class StockOrderProcess implements IStockOrderProcess {
    constructor(
        public barcode?: string,
        public name?: string,
        public quantityRequest?: number,
        public stockInHand?: number,
        public quantityApprove?: number,
        public createdDate?: Moment
    ) {}
}
