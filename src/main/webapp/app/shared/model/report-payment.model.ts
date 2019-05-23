import { Moment } from 'moment';
import { IReportPaymentDetail } from './report-payment-detail.model';

export interface IReportPayment {
    noInvoice?: string;
    customer?: string;
    project?: string;
    saleDate?: Moment;
    totalPayment?: number;
    paymentDetails?: IReportPaymentDetail[];
}

export class ReportPayment implements IReportPayment {
    constructor(
        public noInvoice?: string,
        public customer?: string,
        public project?: string,
        public saleDate?: Moment,
        public totalPayment?: number,
        public paymentDetails?: IReportPaymentDetail[]
    ) {
        this.paymentDetails = [];
    }
}
