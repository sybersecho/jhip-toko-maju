import { Component, OnInit, OnDestroy, AfterContentChecked, AfterViewInit } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ISaleTransactions, SaleTransactions } from 'app/shared/model/sale-transactions.model';
import { Observable } from 'rxjs';
import { SaleTransactionsService } from './sale-transactions.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import * as moment from 'moment';
import { SaleCartService } from './sale-cart.service';
import { CustomerService } from '../customer';

@Component({
    selector: 'jhi-main-cashier',
    templateUrl: './main-cashier.component.html',
    styles: []
})
export class MainCashierComponent implements OnInit, OnDestroy {
    customer: ICustomer;
    defaultCustomer: ICustomer;
    saleTransactions: ISaleTransactions = new SaleTransactions();
    selectedProjectId: number;
    routeData: any;
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
            this.customer = data.customer;
            this.defaultCustomer = this.customer;
        });

        this.selectedProjectId = this.cartService.getProject();
        this.saleTransactions = this.cartService.get();

        // this.getSaleInSession();
        if (!this.saleTransactions.customer) {
            this.setSaleCustomer();
        }

        // this.cartService.setSale(this.saleTransactions);
    }

    ngOnInit() {}

    onPrint() {
        // this.router.navigate(['/', { outlets: { print: 'sale/print/' + sale.noInvoice } }]);
    }

    processAsOrders() {
        this.printAsOrder = true;
        this.save();
    }

    projectChange(project): void {
        this.selectedProjectId = project;
    }

    getCustomerAddress(): string {
        return this.saleTransactions.customerAddress;
    }

    ngOnDestroy() {
        this.cartService.setSale(this.saleTransactions);
        this.cartService.setProject(this.selectedProjectId);
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
        if (this.printAsOrder) {
            // this.onPrint(sale);
        }
        this.saleTransactions = new SaleTransactions();
        this.customer = this.defaultCustomer;
        this.saleTransactions.setCustomer(this.customer);
        this.selectedProjectId = 0;
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

    totalChange(): number {
        return this.saleTransactions.changes();
    }

    protected setSaleCustomer() {
        this.saleTransactions.setCustomer(this.customer);
    }
}
