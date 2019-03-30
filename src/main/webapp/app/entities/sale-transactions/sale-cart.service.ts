import { Injectable } from '@angular/core';
import { ISaleTransactions, SaleTransactions } from 'app/shared/model/sale-transactions.model';
import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { IProduct } from 'app/shared/model/product.model';
import { SaleItem } from 'app/shared/model/sale-item.model';
import { SessionStorageService } from 'ngx-webstorage';

const CART_KEY = 'cart';
const CART_CUSTOMER = 'cart_customer';

@Injectable({
    providedIn: 'root'
})
export class SaleCartService {
    private storage: Storage;
    // private customer: ICustomer;
    private saleTransactions: ISaleTransactions = new SaleTransactions();
    private customer: ICustomer;
    constructor(private storageService: SessionStorageService) {}

    get(): ISaleTransactions {
        return this.retieve();
    }

    getCustomer(): ICustomer {
        return this.retrieveCustomer();
    }

    // getCustomer(){
    //     return this.retieve().c;
    // }

    // updateCustomer(): ICustomer {

    // }

    updateSale(saleTransactions: ISaleTransactions) {
        this.storageService.store(CART_KEY, saleTransactions);
        // console..
    }

    private retrieveCustomer(): ICustomer {
        const customer: ICustomer = this.storageService.retrieve(CART_CUSTOMER);
        if (!customer) {
            this.customer = new Customer();
            this.updateCustomer(this.customer);
        }
        return customer;
    }

    updateCustomer(customer: ICustomer): void {
        this.storageService.store(CART_CUSTOMER, this.customer);
    }

    private retieve(): ISaleTransactions {
        const sale: ISaleTransactions = this.storageService.retrieve('jhi_' + CART_KEY);
        if (!sale) {
            console.log('sale empty');
            this.saleTransactions = new SaleTransactions();
            this.updateSale(this.saleTransactions);
        }
        this.saleTransactions = sale;
        return this.saleTransactions;
    }

    // getCustomer(): ICustomer {
    //     return this.customer !== null ? this.customer : null;
    // }

    // setCustomer(customer: ICustomer) {
    //     this.customer = customer;
    //     this.setSaleCustomer();
    // }

    // addProduct(item: SaleItem): ISaleTransactions {
    //     this.saleTransactions.addOrUpdate(item);
    //     return this.saleTransactions;
    // }

    // updateItemQuantity(i: number, itemQuantity: number): ISaleTransactions {
    //     if (itemQuantity <= 0) {
    //         this.saleTransactions.removeItemAt(i);
    //     } else {
    //         this.saleTransactions.updateItemQuantity(i, itemQuantity);
    //     }

    //     return this.saleTransactions;
    // }

    // removeItem(i: number): ISaleTransactions {
    //     this.saleTransactions.removeItemAt(i);
    //     return this.saleTransactions;
    // }

    // createNew(customer: ICustomer): ISaleTransactions {
    //     this.saleTransactions = new SaleTransactions();
    //     this.setCustomer(customer);
    //     return this.saleTransactions;
    // }

    // protected setSaleCustomer() {
    //     this.saleTransactions.customerId = this.customer.id;
    //     this.saleTransactions.customerFirstName = this.customer.firstName;
    // }
}
