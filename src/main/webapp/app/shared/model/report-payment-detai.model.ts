import { Moment } from 'moment';

export interface IReportPaymentDetail {
    paymentDate?: Moment;
    remainingPayment?: number;
    paid?: number;
}

export class ReportPaymentDetail implements IReportPaymentDetail {
    constructor(public paymentDate?: Moment, public remainingPayment?: number, public paid?: number) {}
}
