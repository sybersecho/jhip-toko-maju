import { Component, OnInit, OnDestroy } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ISaleTransactions, SaleTransactions } from 'app/shared/model/sale-transactions.model';
import { Subscription } from 'rxjs';
import { ISaleItem } from 'app/shared/model/sale-item.model';

@Component({
    selector: 'jhi-main-cashier',
    templateUrl: './main-cashier.component.html',
    styles: []
})
export class MainCashierComponent implements OnInit, OnDestroy {
    customer: ICustomer;
    saleTransactions: ISaleTransactions = new SaleTransactions();
    currentAccount: any;
    routeData: any;
    addItemESubcriber: Subscription;

    constructor(
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
        });

        this.setSaleCustomer();
    }

    ngOnInit() {
        this.registerAddItemEvent();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.addItemESubcriber);
    }

    getFullName(): string {
        return this.customer.firstName + ' ' + this.customer.lastName;
    }

    save() {}

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

    protected setSaleCustomer() {
        this.saleTransactions.customerId = this.customer.id;
        this.saleTransactions.customerFirstName = this.customer.firstName;
    }

    protected registerAddItemEvent(): any {
        this.addItemESubcriber = this.eventManager.subscribe('addItemEvent', response => {
            this.saleTransactions.addOrUpdate(response.item);
        });
    }

    protected push(item: ISaleItem) {
        this.saleTransactions.addOrUpdate(item);
    }
}
