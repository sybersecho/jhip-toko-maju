import { Moment } from 'moment';
import { ISaleItem } from './sale-item.model';

export interface ISaleTransactions {
    id?: number;
    noInvoice?: string;
    discount?: number;
    totalPayment?: number;
    remainingPayment?: number;
    paid?: number;
    saleDate?: Moment;
    items?: ISaleItem[];
    customerFirstName?: string;
    customerId?: number;

    isItemDuplicate(barcode: string): boolean;
    itemPosition(barcode: string): number;
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
        public items?: ISaleItem[],
        public customerFirstName?: string,
        public customerId?: number
    ) {
        this.items = [];
    }

    public isItemDuplicate(barcode: string): boolean {
        const result = this.getItemOnArray(barcode);
        if (result) {
            return true;
        }
        return false;
    }

    public itemPosition(barcode: string): number {
        const result = this.getItemOnArray(barcode);
        if (result) {
            return this.items.indexOf(result);
        }
        return 0;
    }

    private getItemOnArray(barcode: string) {
        return this.items.filter(item => item.product.barcode === barcode)[0];
    }
}
