import { Moment } from 'moment';
import { IPurchaseList, PurchaseList } from 'app/shared/model/purchase-list.model';
import { IProduct } from './product.model';

export interface IPurchase {
    id?: number;
    totalPayment?: number;
    createdDate?: Moment;
    note?: string;
    supplierName?: string;
    supplierId?: number;
    creatorLogin?: string;
    creatorId?: number;
    purchaseLists?: IPurchaseList[];

    addProduct(product: IProduct): Boolean;
    calculateTotalPayment();
    changeQty(i: number);
    removeFromList(i: number);
}

export class Purchase implements IPurchase {
    constructor(
        public id?: number,
        public totalPayment?: number,
        public createdDate?: Moment,
        public note?: string,
        public supplierName?: string,
        public supplierId?: number,
        public creatorLogin?: string,
        public creatorId?: number,
        public purchaseLists?: IPurchaseList[]
    ) {
        this.purchaseLists = [];
        this.totalPayment = 0;
    }

    addProduct(product: IProduct): Boolean {
        if (this.exist(product.barcode)) {
            return false;
        }
        const item = new PurchaseList();
        item.barcode = product.barcode;
        item.name = product.name;
        item.quantity = 1;
        item.total = product.unitPrice;
        item.unit = product.unitName;
        item.unitPrice = product.unitPrice;

        this.purchaseLists.push(item);
        this.calculateTotalPayment();
        return true;
    }

    calculateTotalPayment() {
        this.totalPayment = 0;
        this.purchaseLists.forEach(it => {
            it.calculateTotal();
            this.totalPayment += it.total;
        });
    }

    changeQty(i: number) {
        const item = this.purchaseLists[i];
        if (item.quantity <= 0) {
            this.removeFromList(i);
            return;
        }
        this.calculateTotalPayment();
    }

    removeFromList(i: number) {
        this.purchaseLists.splice(i, 1);
        this.calculateTotalPayment();
    }

    protected exist(barcode: string): Boolean {
        return this.purchaseLists.findIndex(it => it.barcode === barcode) > -1;
    }
}
