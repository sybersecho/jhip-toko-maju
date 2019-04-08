import { Component, OnInit, OnDestroy, AfterContentChecked, AfterViewInit } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ISaleTransactions, SaleTransactions } from 'app/shared/model/sale-transactions.model';
import { Subscription, Observable } from 'rxjs';
import { ISaleItem } from 'app/shared/model/sale-item.model';
import { SaleTransactionsService } from './sale-transactions.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import * as moment from 'moment';
import { SaleCartService } from './sale-cart.service';
import { CustomerService } from '../customer';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';

@Component({
    selector: 'jhi-main-cashier',
    templateUrl: './main-cashier.component.html',
    styles: []
})
export class MainCashierComponent implements OnInit, OnDestroy {
    customer: ICustomer;
    defaultCustomer: ICustomer;
    saleTransactions: ISaleTransactions = new SaleTransactions();
    customerProducts: ICustomerProduct[];
    currentAccount: any;
    routeData: any;
    addItemESubcriber: Subscription;
    changeCustomerSubcriber: Subscription;
    isSale = false;
    printAsOrder = false;

    constructor(
        protected saleService: SaleTransactionsService,
        protected customerService: CustomerService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected eventManager: JhiEventManager,
        protected cartService: SaleCartService
    ) {
        this.routeData = this.activatedRoute.data.subscribe(data => {
            // this.page = data.pagingParams.page;
            // this.previousPage = data.pagingParams.page;
            // this.reverse = data.pagingParams.ascending;
            // this.predicate = data.pagingParams.predicate;
            this.customer = data.customer;
            this.defaultCustomer = this.customer;
        });

        this.saleTransactions = this.cartService.get();
        // this.saleTransactions.setSaleService(this.saleService);
        // this.getSaleInSession();
        if (!this.saleTransactions.customer) {
            this.setSaleCustomer();
        }

        // this.cartService.setSale(this.saleTransactions);
    }

    ngOnInit() {
        this.registerAddItemEvent();
        this.changeCustomerEvent();
        // console.log(this.saleTransactions);
        // this.getSaleInSession();
    }

    onPrint(sale: ISaleTransactions) {
        this.router.navigate(['/', { outlets: { print: 'sale/print/' + sale.noInvoice } }]);
    }

    processAsOrders() {
        this.printAsOrder = true;
        this.save();
    }

    getCustomerCode(): string {
        return this.saleTransactions.customerCode;
    }

    getCustomerFullName(): string {
        return this.saleTransactions.customerFirstName + ' ' + this.saleTransactions.customerLastName;
    }

    getCustomerAddress(): string {
        return this.saleTransactions.customerAddress;
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.addItemESubcriber);
        this.eventManager.destroy(this.changeCustomerSubcriber);
        this.cartService.setSale(this.saleTransactions);
    }

    getFullName(): string {
        return this.customer.firstName + ' ' + this.customer.lastName;
    }

    save() {
        this.saleTransactions.saleDate = moment(new Date());
        this.saleTransactions.recalculate();
        this.subscribeToSaveResponse(this.saleService.create(this.saleTransactions));
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISaleTransactions>>) {
        result.subscribe(
            (res: HttpResponse<ISaleTransactions>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError(res.message)
        );
    }

    protected onSaveSuccess(sale: ISaleTransactions) {
        console.log(sale);
        if (this.printAsOrder) {
            this.onPrint(sale);
        }
        this.saleTransactions = new SaleTransactions();
        this.customer = this.defaultCustomer;
        this.eventManager.broadcast({ name: 'saleSavedEvent' });
        this.setSaleCustomer();
        // this.addSaleIntoSession();
    }

    protected onSaveError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onDeleteItem(itemPos: number) {
        this.saleTransactions.removeItemAt(itemPos);
        // this.addSaleIntoSession();
    }

    onChangeQuantity(i: number, itemQuantity: number) {
        if (itemQuantity <= 0) {
            this.onDeleteItem(i);
        } else {
            this.saleTransactions.updateItemQuantity(i, itemQuantity);
        }
        // this.addSaleIntoSession();
    }

    onPaid() {
        this.saleTransactions.paidTransaction();
        // this.addSaleIntoSession();
    }

    onSearchCustomer() {
        console.log('on search customer');
    }

    totalChange(): number {
        return this.saleTransactions.changes();
    }

    protected setSaleCustomer() {
        this.loadCustomerProduct();
        this.saleTransactions.setCustomer(this.customer);

        // this.addSaleIntoSession();
    }

    // private getSaleInSession() {
    //     this.saleService.getInSession().subscribe(
    //         res => {
    //             console.log('response from get sale in session!!!');
    //             console.log(res);
    //             this.saleTransactions = res.body;
    //         },
    //         err => {
    //             console.log('Error: ' + err.message);
    //         }
    //     );
    // }

    // private addSaleIntoSession() {
    //     if (this.saleTransactions) {
    //         this.saleService.addToSession(this.saleTransactions).subscribe(res => {
    //             console.log('success call add to session');
    //         });
    //     }
    // }

    protected registerAddItemEvent(): any {
        this.addItemESubcriber = this.eventManager.subscribe('addItemEvent', response => {
            let item: ISaleItem = response.item;
            item = this.updateSalePrice(item);
            this.saleTransactions.addOrUpdate(item);
        });
    }

    loadCustomerProduct(): void {
        this.customerService.searcyByCustomer(this.customer.id).subscribe(
            response => {
                this.customerProducts = response.body;
                // console.log(this.customerProducts);
                this.updateExistingItemPrice();
            },
            err => {
                console.error(err.message);
            }
        );
    }

    updateSalePrice(item: ISaleItem): ISaleItem {
        item = this.findAndUpdateSalePrice(item);
        return item;
    }

    private findAndUpdateSalePrice(item: ISaleItem): ISaleItem {
        let cusItem: ICustomerProduct = null;
        if (this.customerProducts) {
            cusItem = this.customerProducts.find(product => product.id === item.productId);
        }
        if (cusItem) {
            // console.log('Price Before: ' + item.sellingPrice);
            item.sellingPrice = cusItem.specialPrice;
            item.createItem();
            // console.log('Price after: ' + item.sellingPrice);
            return item;
        }
        return null;
    }

    protected changeCustomerEvent(): any {
        this.changeCustomerSubcriber = this.eventManager.subscribe('onSelectCustomerEvent', response => {
            this.customer = response.data;
            this.setSaleCustomer();
        });
    }

    protected updateExistingItemPrice(): void {
        this.saleTransactions.items.forEach(existingItem => {
            let found: ICustomerProduct = null;

            // console.log('Sell Price Before: ' + existingItem.sellingPrice);
            found = this.findAndUpdateSalePrice(existingItem);

            if (!found) {
                // console.log('Product sale price: ' + existingItem.product.sellingPrice);
                existingItem.sellingPrice = existingItem.product.sellingPrice;
                // console.log('Sell Price after not found: ' + existingItem.sellingPrice);
            }
            existingItem.createItem();
        });
        this.saleTransactions.recalculate();
    }

    protected push(item: ISaleItem): void {
        this.saleTransactions.addOrUpdate(item);
    }
}
