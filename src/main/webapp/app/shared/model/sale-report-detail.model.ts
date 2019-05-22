import { ISaleTransactions } from './sale-transactions.model';
import { ISaleItem } from './sale-item.model';
import { ISaleReportProduct, SaleReportProduct } from './sale-report-product.model';
import { Moment } from 'moment';
import { DATE_TIME_S_FORMAT } from '../constants/input.constants';

export interface ISaleReportDetail {
    customerFullName?: string;
    projectName?: string;
    totalPayment?: number;
    products?: ISaleReportProduct[];

    createFrom(sale: ISaleTransactions);
}

export class SaleReportDetail implements ISaleReportDetail {
    constructor(
        public customerFullName?: string,
        public projectName?: string,
        public totalPayment?: number,
        public products?: ISaleReportProduct[]
    ) {
        this.products = [];
    }

    createFrom(sale: ISaleTransactions) {
        this.customerFullName = sale.customerFirstName + ' ' + sale.customerLastName;
        this.projectName = this.checkProjectName(sale.projectName);
        this.totalPayment = sale.totalPayment;
        this.createProducts(sale.items, sale.saleDate);
    }

    protected createProducts(items: ISaleItem[], saleDate: Moment): void {
        items.forEach(item => this.products.push(this.createProduct(item, saleDate)));
    }

    protected createProduct(item: ISaleItem, saleDate: Moment): SaleReportProduct {
        return new SaleReportProduct(
            item.barcode,
            item.productName,
            item.unit,
            item.sellingPrice,
            item.quantity,
            item.totalPrice,
            saleDate.format(DATE_TIME_S_FORMAT)
        );
    }

    protected checkProjectName(projectName: string): string {
        if (projectName) {
            return projectName;
        }
        return '-';
    }
}
