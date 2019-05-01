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
    customerId?: number;
    customerFirstName?: string;
    customerLastName?: string;
    customerFullName?: string;
    isEdit?: boolean;
    saldo?: number;
    projectName?: string;
    projectId?: number;
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
        public customerId?: number,
        public customerFirstName?: string,
        public customerLastName?: string,
        public customerFullName?: string,
        public isEdit?: boolean,
        public saldo?: number,
        public projectName?: string,
        public projectId?: number
    ) {
        this.settled = this.settled || false;
        this.customerFullName = this.customerFirstName + ' ' + this.customerLastName;
        this.isEdit = this.isEdit || false;
    }
}
