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
    noTransaction?: string;
    cashReturned?: boolean;
    creatorLogin?: string;
    creatorId?: number;
    customerCode?: string;
    customerId?: number;
    supplierCode?: string;
    supplierId?: number;
    returnItems?: IReturnItem[];

    addItem(item: IReturnItem);
    calculateTotalReturn();
    removeItemAt(itemPos: number);
    updateQuantity(i: number, itemQuantity: number);
}

export class ReturnTransaction implements IReturnTransaction {
    constructor(
        public id?: number,
        public created_date?: Moment,
        public transactionType?: TransactionType,
        public totalPriceReturn?: number,
        public noTransaction?: string,
        public cashReturned?: boolean,
        public creatorLogin?: string,
        public creatorId?: number,
        public customerCode?: string,
        public customerId?: number,
        public supplierCode?: string,
        public supplierId?: number,
        public returnItems?: IReturnItem[]
    ) {
        this.returnItems = [];
        this.cashReturned = this.cashReturned || false;
    }

    addItem(item: IReturnItem) {
        const exist = this.isItemExis(item.barcode);
        if (exist) {
            const index = this.returnItems.indexOf(exist);
            this.updateItem(item, index);
        } else {
            this.returnItems.push(item);
        }
    }

    removeItemAt(index: number) {
        this.returnItems.splice(index, 1);
        this.calculateTotalReturn();
    }

    updateQuantity(i: number, quantity: number) {
        const changeItem = this.returnItems[i];
        changeItem.quantity = quantity;
        changeItem.createItem();
        this.returnItems[i] = changeItem;

        this.calculateTotalReturn();
    }

    protected updateItem(item: IReturnItem, index: number): void {
        const currentItem = this.returnItems[index];
        currentItem.quantity += item.quantity;
        currentItem.unitPrice = item.unitPrice;
        currentItem.createItem();
        this.returnItems[index] = currentItem;
        this.calculateTotalReturn();
    }

    calculateTotalReturn() {
        this.totalPriceReturn = 0;
        this.returnItems.forEach(item => {
            this.totalPriceReturn += item.totalItemPrice;
        });
    }

    protected isItemExis(barcode: string) {
        return this.returnItems.filter(item => item.barcode === barcode)[0];
    }
}
