import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IInvoice } from 'app/shared/model/invoice.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE, DATE_FORMAT, DATE_TIME_S_FORMAT } from 'app/shared';
import { InvoiceService } from './invoice.service';
import { SearchInvoice } from './search-filter.component';
import { ExcelService } from 'app/shared/export/excel.service';
import { ExcelModel } from 'app/shared/export/excel-model';

@Component({
    selector: 'jhi-invoice',
    templateUrl: './invoice.component.html'
})
export class InvoiceComponent implements OnInit, OnDestroy {
    invoices: IInvoice[];
    searchCriteria: SearchInvoice;
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;
    invoiceExcel: invoiceExcel[];

    constructor(
        protected invoiceService: InvoiceService,
        protected jhiAlertService: JhiAlertService,
        protected excelService: ExcelService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected accountService: AccountService
    ) {
        this.invoices = [];
        this.invoiceExcel = [];
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
        if (this.searchCriteria) {
            const queryCriteria: any = {};
            queryCriteria.page = this.page;
            queryCriteria.size = this.itemsPerPage;
            queryCriteria.sort = this.sort();
            queryCriteria.start = moment(this.searchCriteria.fromDate)
                .startOf('day')
                .toJSON();
            queryCriteria.end = moment(this.searchCriteria.endDate)
                .endOf('day')
                .toJSON();
            if (this.searchCriteria.customer) {
                queryCriteria.customer = this.searchCriteria.customer.id;
            }
            if (this.searchCriteria.project) {
                queryCriteria.project = this.searchCriteria.project.id;
            }
            if (this.searchCriteria.noInvoice) {
                queryCriteria.invoice = this.searchCriteria.noInvoice;
            }
            this.invoiceService
                .query(queryCriteria)
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

    print(noInvoice: string) {
        this.router.navigate(['/', { outlets: { print: 'sale/print/' + noInvoice } }]);
    }

    exportToExcel(): void {
        const excelModel = new ExcelModel();
        excelModel.data = this.invoiceExcel;
        excelModel.fileName = 'Daftar Penjualan';
        excelModel.header = ['No Fraktur', 'Tanggal', 'Pelanggan', 'Project', 'Total', 'Sisa Pembayaran', 'Pembayan'];
        excelModel.title = 'Daftar Penjualan';
        excelModel.workSheetName = 'Penjualan';

        this.excelService.generateExcel(excelModel);
    }
    searchEvt(event) {
        this.invoices = [];
        this.searchCriteria = event;
        this.loadAll();
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
        this.invoices = [];
        this.invoiceExcel = [];
        for (let i = 0; i < data.length; i++) {
            data[i].projectName = data[i].projectName ? data[i].projectName : '-';
            this.invoiceExcel.push(
                new InvoiceExcel(
                    data[i].noInvoice,
                    data[i].customer,
                    data[i].totalPayment,
                    data[i].remainingPayment,
                    data[i].paid,
                    data[i].saleDate.format(DATE_TIME_S_FORMAT),
                    data[i].paid,
                    data[i].projectName
                )
            );
            this.invoices.push(data[i]);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

class InvoiceExcel {
    constructor(
        public noInvoice?: number,
        public customer?: string,
        public totalPayment?: number,
        public remainingPayment?: number,
        public paid?: number,
        public saleDate?: string,
        public settlement?: number,
        public projectName?: string
    ) {}
}
