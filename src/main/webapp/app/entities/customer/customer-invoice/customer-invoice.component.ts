import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { InvoiceService } from 'app/entities/invoice';
// import moment = require('moment');
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { IInvoice } from 'app/shared/model/invoice.model';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from 'app/core';
import { ITEMS_PER_PAGE } from 'app/shared';

@Component({
    selector: 'jhi-customer-invoice',
    templateUrl: './customer-invoice.component.html',
    styles: []
})
export class CustomerInvoiceComponent implements OnInit {
    @Input() customerInv: ICustomer;
    @Output() countSale = new EventEmitter();
    invoices: IInvoice[];
    // totalPaid = 0;
    // remainingPayment = 0;
    page: any;
    itemsPerPage: number;
    predicate: any;
    reverse: any;
    links: any;
    totalItems: number;

    constructor(
        protected invoiceService: InvoiceService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.invoices = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
    }

    ngOnInit() {
        this.loadAll();
    }

    loadAll() {
        // if (this.searchInvoice) {
        //     console.log('searchInvoice');
        this.invoiceService
            .queryByCustomer({
                // query: this.currentSearch,
                customer: this.customerInv.id,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IInvoice[]>) => this.paginateInvoices(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        return;
        // }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    protected paginateInvoices(data: IInvoice[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        let totalPayment = 0;
        let totalRemainingPay = 0;
        const totalInvoice = this.totalItems;
        for (let i = 0; i < data.length; i++) {
            this.invoices.push(data[i]);
            totalPayment += data[i].totalPayment;
            totalRemainingPay += data[i].remainingPayment;
        }

        this.countSale.next({ totalInvoice, totalPayment, totalRemainingPay });
    }

    trackId(index: number, item: IInvoice) {
        return item.id;
    }

    loadPage(page) {
        this.page = page;
        // this.loadAll();
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
}
