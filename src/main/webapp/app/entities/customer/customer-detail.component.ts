import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomer } from 'app/shared/model/customer.model';
import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService, SearchInvoice } from '../invoice';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { Subscription } from 'rxjs';
import { ITEMS_PER_PAGE } from 'app/shared';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';

@Component({
    selector: 'jhi-customer-detail',
    templateUrl: './customer-detail.component.html'
})
export class CustomerDetailComponent implements OnInit {
    customer: ICustomer;
    // invoices: IInvoice[];
    productCount = 0;
    invoiceCount = 0;
    totalBuy = 0;
    totalRemainingPayment = 0;
    totalProject = 0;
    // customerProducts: ICustomerProduct[];
    // searchInvoice: SearchInvoice;
    // currentAccount: any;
    // eventSubscriber: Subscription;
    // itemsPerPage: number;
    // links: any;
    // page: any;
    // predicate: any;
    // reverse: any;
    // totalItems: number;
    // currentSearch: string;

    constructor(
        // protected invoiceService: InvoiceService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        // this.invoices = [];
        // this.itemsPerPage = ITEMS_PER_PAGE;
        // this.page = 0;
        // this.links = {
        //     last: 0
        // };
        // this.predicate = 'id';
        // this.reverse = true;
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customer }) => {
            this.customer = customer;
        });
    }

    invoice(sale) {
        console.log(sale);
        this.invoiceCount = sale.totalInvoice;
        this.totalBuy = sale.totalPayment;
        this.totalRemainingPayment = sale.totalRemainingPay;
        // this.invoiceCount = i;
    }

    project(count: number) {
        console.log('project: ' + count);
        this.totalProject = count;
    }

    product(count: number) {
        console.log('Product: ' + count);
        this.productCount = count;
    }

    // loadPage(page) {
    //     this.page = page;
    //     // this.loadAll();
    // }

    // trackId(index: number, item: IInvoice) {
    //     return item.id;
    // }

    previousState() {
        window.history.back();
    }
}
