import { Moment } from 'moment';

export interface IInvoice {
    id?: number;
    noInvoice?: number;
    customer?: string;
    totalPayment?: number;
    remainingPayment?: number;
    paid?: number;
    saleDate?: Moment;
    settlement?: number;
}

export class Invoice implements IInvoice {
    constructor(
        public id?: number,
        public noInvoice?: number,
        public customer?: string,
        public totalPayment?: number,
        public remainingPayment?: number,
        public paid?: number,
        public saleDate?: Moment,
        public settlement?: number
    ) {
        this.settlement = 0;
    }
}
