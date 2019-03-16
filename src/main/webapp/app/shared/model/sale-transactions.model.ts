import { Moment } from 'moment';
// import { ISaleItem } from 'app/shared/model/sale-item.model';

export interface ISaleTransactions {
    id?: number;
    noInvoice?: string;
    discount?: number;
    totalPayment?: number;
    remainingPayment?: number;
    paid?: number;
    saleDate?: Moment;
    // items?: ISaleItem[];
}

export class SaleTransactions implements ISaleTransactions {
    constructor(
        public id?: number,
        public noInvoice?: string,
        public discount?: number,
        public totalPayment?: number,
        public remainingPayment?: number,
        public paid?: number,
        public saleDate?: Moment // public items?: ISaleItem[]
    ) {}
}
