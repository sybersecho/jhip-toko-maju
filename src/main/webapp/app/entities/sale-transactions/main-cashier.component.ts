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

    constructor(protected activatedRoute: ActivatedRoute, protected cartService: SaleCartService) {
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

    projectChange(project): void {
        this.selectedProjectId = project;
    }

    ngOnDestroy() {
        this.cartService.setSale(this.saleTransactions);
        this.cartService.setProject(this.selectedProjectId);
    }

    protected setSaleCustomer() {
        this.saleTransactions.setCustomer(this.customer);
    }
}
