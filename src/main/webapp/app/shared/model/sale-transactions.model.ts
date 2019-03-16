import { Moment } from 'moment';

export interface ISaleTransactions {
    id?: number;
    noInvoice?: string;
    discount?: number;
    totalPayment?: number;
    remainingPayment?: number;
    paid?: number;
    saleDate?: Moment;
    // items?: ISaleItem[];
    customerFirstName?: string;
    customerId?: number;
}

export class SaleTransactions implements ISaleTransactions {
    constructor(
        public id?: number,
        public noInvoice?: string,
        public discount?: number,
        public totalPayment?: number,
        public remainingPayment?: number,
        public paid?: number,
        public saleDate?: Moment,
        // public items?: ISaleItem[],
        public customerFirstName?: string,
        public customerId?: number
    ) {}
}
