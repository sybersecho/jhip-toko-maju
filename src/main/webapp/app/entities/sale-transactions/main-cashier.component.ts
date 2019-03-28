import { Component, OnInit, OnDestroy } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ISaleTransactions, SaleTransactions } from 'app/shared/model/sale-transactions.model';
import { Subscription, Observable } from 'rxjs';
import { ISaleItem } from 'app/shared/model/sale-item.model';
import { SaleTransactionsService } from './sale-transactions.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

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
        protected eventManager: JhiEventManager
    ) {
        this.routeData = this.activatedRoute.data.subscribe(data => {
            // this.page = data.pagingParams.page;
            // this.previousPage = data.pagingParams.page;
            // this.reverse = data.pagingParams.ascending;
            // this.predicate = data.pagingParams.predicate;
            this.customer = data.customer;
            this.defaultCustomer = this.customer;
        });

        this.setSaleCustomer();
    }

    ngOnInit() {
        this.registerAddItemEvent();
        this.changeCustomerEvent();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.addItemESubcriber);
        this.eventManager.destroy(this.changeCustomerSubcriber);
    }

    getFullName(): string {
        return this.customer.firstName + ' ' + this.customer.lastName;
    }

    save() {
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
    }

    protected onSaveError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onDeleteItem(itemPos: number) {
        this.saleTransactions.removeItemAt(itemPos);
    }

    onChangeQuantity(i: number, itemQuantity: number) {
        if (itemQuantity <= 0) {
            this.onDeleteItem(i);
        } else {
            this.saleTransactions.updateItemQuantity(i, itemQuantity);
        }
    }

    onPaid() {
        this.saleTransactions.paidTransaction();
    }

    onSearchCustomer() {
        console.log('on search customer');
    }

    protected setSaleCustomer() {
        this.saleTransactions.customerId = this.customer.id;
        this.saleTransactions.customerFirstName = this.customer.firstName;
    }

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
