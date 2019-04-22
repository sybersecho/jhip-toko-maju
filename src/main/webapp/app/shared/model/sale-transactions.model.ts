import { Moment } from 'moment';
import { ISaleItem } from './sale-item.model';
import { ICustomer } from './customer.model';
import { SaleTransactionsService } from 'app/entities/sale-transactions';

export interface ISaleTransactions {
    id?: number;
    noInvoice?: string;
    discount?: number;
    totalPayment?: number;
    remainingPayment?: number;
    paid?: number;
    saleDate?: Moment;
    items?: Array<ISaleItem>;
    customerFirstName?: string;
    customerId?: number;
    customerLastName?: string;
    customerCode?: string;
    customerAddress?: string;
    creatorLogin?: string;
    creatorId?: number;
    settled?: boolean;

    customer?: ICustomer;

    // isItemDuplicate(barcode: string): boolean;
    // itemPosition(barcode: string): number;
    // calculateTotalPayment(): void;
    removeItemAt(i: number): void;
    addItem(item: ISaleItem): void;
    updateItem(item: ISaleItem): void;
    updateItemQuantity(itemIndex: number, newQuantity: number): void;
    addOrUpdate(item: ISaleItem): void;
    paidTransaction(): void;
    changes(): number;
    recalculate(): void;
    setCustomer(customer: ICustomer): void;
    // setSaleService(service: SaleTransactionsService): void;
}

export class SaleTransactions implements ISaleTransactions {
    // private saletransactionService: SaleTransactionsService;

    constructor(
        public id?: number,
        public noInvoice?: string,
        public discount?: number,
        public totalPayment?: number,
        public remainingPayment?: number,
        public paid?: number,
        public saleDate?: Moment,
        public items?: Array<ISaleItem>,
        public customerFirstName?: string,
        public customerId?: number,
        public customerLastName?: string,
        public customerCode?: string,
        public customerAddress?: string,
        public creatorLogin?: string,
        public creatorId?: number,
        public settled?: boolean,
        public customer?: ICustomer
    ) {
        this.items = [];
        this.settled = this.settled || false;
    }

    // public setSaleService(service: SaleTransactionsService): void {
    //     this.saletransactionService = service;
    // }

    public removeItemAt(i: number): void {
        this.items.splice(i, 1);
        this.calculateTotalPayment();
        // this.addToSession();
        // this.saletransactionService.
    }

    // private addToSession() {
    //     if (this.saletransactionService) {
    //         // console.log('is this null: ' + this);
    //         this.saletransactionService.addToSession(this).subscribe(res => {
    //             console.log('success add to session');
    //         });
    //     }
    // }

    public addItem(item: ISaleItem): void {
        this.items.push(item);
        this.calculateTotalPayment();
        // this.addToSession();
    }

    public setCustomer(customer: ICustomer): void {
        this.customer = customer;
        this.customerId = customer.id;
        this.customerFirstName = customer.firstName;
        this.customerLastName = customer.lastName;
        this.customerCode = customer.code;
        this.customerAddress = customer.address;
        // this.addToSession();
    }

    public updateItem(item: ISaleItem): void {
        const itemPos = this.itemPosition(item.product.barcode);
        const updateItem = this.items[itemPos];
        updateItem.quantity += item.quantity;
        updateItem.totalPrice += item.totalPrice;
        this.items[itemPos] = updateItem;
        this.calculateTotalPayment();
        // this.addToSession();
    }

    public updateItemQuantity(itemIndex: number, newQuantity: number): void {
        const changeItem = this.items[itemIndex];
        changeItem.quantity = newQuantity;
        changeItem.totalPrice = changeItem.quantity * changeItem.sellingPrice;

        this.items[itemIndex] = changeItem;
        this.calculateTotalPayment();
        // this.addToSession();
    }

    recalculate(): void {
        this.settled = false;
        if (this.paid >= this.totalPayment) {
            this.paid = this.totalPayment;
            this.settled = true;
        }
        this.calculateTotalPayment();
        // this.addToSession();
    }

    public addOrUpdate(item: ISaleItem): void {
        if (this.isItemDuplicate(item.product.barcode)) {
            this.updateItem(item);
        } else {
            this.addItem(item);
        }
        // this.addToSession();
    }

    public paidTransaction(): void {
        this.calculateRemainPayment();
        // this.addToSession();
    }

    public changes(): number {
        return Math.abs(this.totalPayment - this.discount - this.paid);
    }

    private getItemOnArray(barcode: string) {
        return this.items.filter(item => item.product.barcode === barcode)[0];
    }

    private isItemDuplicate(barcode: string): boolean {
        const result = this.getItemOnArray(barcode);
        if (result) {
            return true;
        }
        return false;
    }

    private itemPosition(barcode: string): number {
        const result = this.getItemOnArray(barcode);
        if (result) {
            return this.items.indexOf(result);
        }
        return 0;
    }

    private calculateTotalPayment(): void {
        this.totalPayment = 0;
        this.items.forEach(item => {
            this.totalPayment += item.totalPrice;
        });
        this.calculateRemainPayment();
    }

    private calculateRemainPayment(): void {
        // this.settled = false;
        if (!this.paid) {
            this.paid = 0;
        }
        if (!this.discount) {
            this.discount = 0;
        }
        this.paid = Math.abs(this.paid);
        this.discount = Math.abs(this.discount);
        this.remainingPayment = this.totalPayment - this.discount - this.paid;
        this.remainingPayment = Math.abs(this.remainingPayment);
        this.checkRemainPayment();
    }

    private checkRemainPayment(): void {
        if (this.remainingPayment < 0) {
            this.remainingPayment = 0;
            this.settled = true;
        }
    }
}
