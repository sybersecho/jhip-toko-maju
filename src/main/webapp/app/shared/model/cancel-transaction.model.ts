import { Moment } from 'moment';

export interface ICancelTransaction {
    id?: number;
    noInvoice?: string;
    cancelDate?: Moment;
    note?: string;
    saleTransactionsNoInvoice?: string;
    saleTransactionsId?: number;
}

export class CancelTransaction implements ICancelTransaction {
    constructor(
        public id?: number,
        public noInvoice?: string,
        public cancelDate?: Moment,
        public note?: string,
        public saleTransactionsNoInvoice?: string,
        public saleTransactionsId?: number
    ) {}
}
