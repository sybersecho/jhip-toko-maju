import { Moment } from 'moment';

export interface IDuePayment {
    id?: number;
    remainingPayment?: number;
    createdDate?: Moment;
    settled?: boolean;
    paid?: number;
    totalPayment?: number;
    creatorLogin?: string;
    creatorId?: number;
    saleNoInvoice?: string;
    saleId?: number;
    customerFirstName?: string;
    customerLastName?: string;
    customerFullName?: string;
    isEdit?: boolean;
}

export class DuePayment implements IDuePayment {
    constructor(
        public id?: number,
        public remainingPayment?: number,
        public createdDate?: Moment,
        public settled?: boolean,
        public paid?: number,
        public totalPayment?: number,
        public creatorLogin?: string,
        public creatorId?: number,
        public saleNoInvoice?: string,
        public saleId?: number,
        public customerFirstName?: string,
        public customerLastName?: string,
        public customerFullName?: string,
        public isEdit?: boolean
    ) {
        this.settled = this.settled || false;
        this.customerFullName = this.customerFirstName + ' ' + this.customerLastName;
        this.isEdit = this.isEdit || false;
    }
}
