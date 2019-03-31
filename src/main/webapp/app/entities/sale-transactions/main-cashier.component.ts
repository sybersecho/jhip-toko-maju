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

@Component({
    selector: 'jhi-main-cashier',
    templateUrl: './main-cashier.component.html',
    styles: []
})
export class MainCashierComponent implements OnInit, OnDestroy {
    customer: ICustomer;
    defaultCustomer: ICustomer;
    saleTransactions: ISaleTransactions = new SaleTransactions();
    currentAccount: any;
    routeData: any;
    addItemESubcriber: Subscription;
    changeCustomerSubcriber: Subscription;

    constructor(
        protected saleService: SaleTransactionsService,
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

    getCustomerCode(): string {
        // this.saleTransactions.c
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
        // console.log('ondestroy');
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
            (res: HttpResponse<ISaleTransactions>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError(res.message)
        );
    }

    protected onSaveSuccess() {
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
        // this.saleTransactions.customerId = this.customer.id;
        // this.saleTransactions.customerFirstName = this.customer.firstName;
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
            this.saleTransactions.addOrUpdate(response.item);
        });
    }

    protected changeCustomerEvent(): any {
        this.changeCustomerSubcriber = this.eventManager.subscribe('onSelectCustomerEvent', response => {
            this.customer = response.data;
            this.setSaleCustomer();
        });
    }

    protected push(item: ISaleItem) {
        this.saleTransactions.addOrUpdate(item);
    }
}
