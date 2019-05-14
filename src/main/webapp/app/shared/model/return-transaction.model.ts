import { Moment } from 'moment';
import { IReturnItem } from 'app/shared/model/return-item.model';

export const enum TransactionType {
    SHOP = 'SHOP',
    SUPPLIER = 'SUPPLIER'
}

export interface IReturnTransaction {
    id?: number;
    created_date?: Moment;
    transactionType?: TransactionType;
    totalPriceReturn?: number;
    creatorLogin?: string;
    creatorId?: number;
    customerCode?: string;
    customerId?: number;
    supplierCode?: string;
    supplierId?: number;
    returnItems?: IReturnItem[];
}

export class ReturnTransaction implements IReturnTransaction {
    constructor(
        public id?: number,
        public created_date?: Moment,
        public transactionType?: TransactionType,
        public totalPriceReturn?: number,
        public creatorLogin?: string,
        public creatorId?: number,
        public customerCode?: string,
        public customerId?: number,
        public supplierCode?: string,
        public supplierId?: number,
        public returnItems?: IReturnItem[]
    ) {
        this.returnItems = [];
    }
}
