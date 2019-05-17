export interface ISaleReport {
    noInvoice?: String;
    creatorLogin?: string;
    customerFullName?: string;
    totalPayment?: number;
    discount?: number;
    paid?: number;
    remainingPayment?: number;
    createdDate?: String;
}

export class SaleReport implements ISaleReport {
    constructor(
        public noInvoice?: String,
        public customerFullName?: string,
        public totalPayment?: number,
        public discount?: number,
        public paid?: number,
        public remainingPayment?: number,
        public creatorLogin?: string,
        public createdDate?: String
    ) {}
}
