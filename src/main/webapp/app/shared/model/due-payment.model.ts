import { Moment } from 'moment';

export interface IDuePayment {
    id?: number;
    remainingPayment?: number;
    createdDate?: Moment;
    settled?: boolean;
    paid?: number;
    creatorLogin?: string;
    creatorId?: number;
    saleNoInvoice?: string;
    saleId?: number;
}

export class DuePayment implements IDuePayment {
    constructor(
        public id?: number,
        public remainingPayment?: number,
        public createdDate?: Moment,
        public settled?: boolean,
        public paid?: number,
        public creatorLogin?: string,
        public creatorId?: number,
        public saleNoInvoice?: string,
        public saleId?: number
    ) {
        this.settled = this.settled || false;
    }
}
