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
        this.loadCustomerProduct();
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
        console.log('is settled? ' + this.saleTransactions.settled);
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
            const item: ISaleItem = response.item;
            const price = this.getCustomerProductPrice(item.productId);
            item.sellingPrice = price > 0 ? price : item.sellingPrice;
            item.createItem();
            this.saleTransactions.addOrUpdate(item);
        });
    }

    private getCustomerProductPrice(productId: number): number {
        const find: ICustomerProduct = this.findItemInCustomerProductList(productId);
        return find ? find.specialPrice : -1;
    }

    private findItemInCustomerProductList(productId: number): ICustomerProduct {
        let cusItem: ICustomerProduct = null;
        if (this.customerProducts && this.customerProducts.length > 0) {
            cusItem = this.customerProducts.find(product => product.productId === productId);
        }
        return cusItem;
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

    protected changeCustomerEvent(): any {
        this.changeCustomerSubcriber = this.eventManager.subscribe('onSelectCustomerEvent', response => {
            this.customer = response.data;
            this.setSaleCustomer();
        });
    }

    protected updateExistingItemPrice(): void {
        this.saleTransactions.items.forEach(existingItem => {
            // console.log('Sell Price Before: ' + existingItem.sellingPrice);
            const found = this.getCustomerProductPrice(existingItem.productId);

            existingItem.sellingPrice = found > 0 ? found : existingItem.product.sellingPrice;

            existingItem.createItem();
        });
        this.saleTransactions.recalculate();
    }

    protected push(item: ISaleItem): void {
        this.saleTransactions.addOrUpdate(item);
    }
}
