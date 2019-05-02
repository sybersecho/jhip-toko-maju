import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IInvoice } from 'app/shared/model/invoice.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE, DATE_FORMAT } from 'app/shared';
import { InvoiceService } from './invoice.service';
import { SearchInvoice } from './search-filter.component';

@Component({
    selector: 'jhi-invoice',
    templateUrl: './invoice.component.html'
})
export class InvoiceComponent implements OnInit, OnDestroy {
    invoices: IInvoice[];
    searchInvoice: SearchInvoice;
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;

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
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.searchInvoice) {
            console.log('searchInvoice');
            this.invoiceService
                .queryByDate({
                    // query: this.currentSearch,
                    start: moment(this.searchInvoice.fromDate)
                        .startOf('day')
                        .toJSON(),
                    end: moment(this.searchInvoice.endDate)
                        .endOf('day')
                        .toJSON(),
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IInvoice[]>) => this.paginateInvoices(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        // this.invoiceService
        //     .queryByDate({
        //         page: this.page,
        //         size: this.itemsPerPage,
        //         sort: this.sort(),
        //         start: moment(new Date(), DATE_FORMAT).format('DD-MM-YYYY'),
        //         end: moment(new Date(), DATE_FORMAT).format('DD-MM-YYYY')
        //     })
        //     .subscribe(
        //         (res: HttpResponse<IInvoice[]>) => this.paginateInvoices(res.body, res.headers),
        //         (res: HttpErrorResponse) => this.onError(res.message)
        //     );
        // console.log(moment(new Date(), DATE_FORMAT));
    }

    searchEvt(event) {
        this.invoices = event;
        // this.searchInvoice = event;
        // this.loadAll();
    }

    clearEvt(event) {
        this.invoices = [];
    }
    // reset
    reset() {
        this.page = 0;
        this.invoices = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.invoices = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.invoices = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInInvoices();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IInvoice) {
        return item.id;
    }

    registerChangeInInvoices() {
        this.eventSubscriber = this.eventManager.subscribe('invoiceListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateInvoices(data: IInvoice[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.invoices.push(data[i]);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
