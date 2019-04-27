import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription, from } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IDuePayment, DuePayment } from 'app/shared/model/due-payment.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE, DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared';
import { DuePaymentService } from './due-payment.service';
import { SaleTransactionsService } from '../sale-transactions';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { DatePipe } from '@angular/common';
import * as moment from 'moment';

@Component({
    selector: 'jhi-due-payment',
    templateUrl: './due-payment.component.html'
})
export class DuePaymentComponent implements OnInit, OnDestroy {
    duePayments: IDuePayment[];
    sales: ISaleTransactions[];
    totalNominal: number;
    totalSaldo: number;
    totalPaid: number;
    fromDate: string;
    endDate: string;
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
        protected duePaymentService: DuePaymentService,
        protected saleTransactionsService: SaleTransactionsService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService,
        protected datePipe: DatePipe
    ) {
        this.duePayments = [];
        this.sales = [];
        this.totalNominal = 0;
        this.totalSaldo = 0;
        this.totalPaid = 0;
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
        // if (this.currentSearch) {
        //     this.duePaymentService
        //         .search({
        //             query: this.currentSearch,
        //             page: this.page,
        //             size: this.itemsPerPage,
        //             sort: this.sort()
        //         })
        //         .subscribe(
        //             (res: HttpResponse<IDuePayment[]>) => this.paginateDuePayments(res.body, res.headers),
        //             (res: HttpErrorResponse) => this.onError(res.message)
        //         );
        //     return;
        // }
        // this.duePaymentService
        //     .query({
        //         page: this.page,
        //         size: this.itemsPerPage,
        //         sort: this.sort()
        //     })
        //     .subscribe(
        //         (res: HttpResponse<IDuePayment[]>) => this.paginateDuePayments(res.body, res.headers),
        //         (res: HttpErrorResponse) => this.onError(res.message)
        //     );

        this.saleTransactionsService
            .queryDueTransaction({
                from: moment(this.fromDate)
                    .startOf('day')
                    .toJSON(),
                end: moment(this.endDate)
                    .endOf('day')
                    .toJSON()
            })
            .subscribe(
                (res: HttpResponse<ISaleTransactions[]>) => this.paginateDuePayments(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    save() {
        const tempDue: IDuePayment[] = this.setCreatedDate(this.duePayments.filter(due => due.isEdit));

        const updateSale: ISaleTransactions[] = this.sales.filter(sale => {
            return tempDue.findIndex(due => due.saleId === sale.id) > -1;
        });
        // console.log('temp: ');
        // console.log(tempDue);
        // console.log('sale: ');
        // console.log(updateSale);
        this.duePaymentService.saveDuePayment(tempDue, this.sales).subscribe(
            res => {
                console.log('success');
                this.loadAll();
            },
            err => {
                console.log(err.message);
            }
        );
    }

    protected setCreatedDate(dues: IDuePayment[]): IDuePayment[] {
        dues.forEach(due => {
            due.createdDate = moment(new Date(), DATE_TIME_FORMAT);
        });

        return dues;
    }

    reset() {
        this.page = 0;
        this.duePayments = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.duePayments = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search() {
        // if (!query) {
        //     return this.clear();
        // }
        this.duePayments = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.currentMonth();
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
            // console.log(this.currentAccount);
        });
        this.registerChangeInDuePayments();
    }

    protected currentMonth() {
        const dateFormat = 'yyyy-MM-dd';
        // 'yyyy-MM-dd';
        const today = new Date();
        const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
        const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
        this.fromDate = this.datePipe.transform(firstDay, dateFormat);
        this.endDate = this.datePipe.transform(lastDay, dateFormat);
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDuePayment) {
        return item.id;
    }

    registerChangeInDuePayments() {
        this.eventSubscriber = this.eventManager.subscribe('duePaymentListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    changeEdit(duePayment: IDuePayment) {
        if (duePayment.isEdit) {
            duePayment.isEdit = false;
            duePayment.paid = 0;
        } else {
            duePayment.isEdit = true;
            duePayment.paid = duePayment.saldo;
        }
        this.calculateTotalPaid();
    }

    calculateTotalPaid() {
        this.totalPaid = 0;
        this.duePayments.forEach(due => {
            this.totalPaid += due.paid;
            this.calculateRemainingPayment(due);
        });
    }

    calculateRemainingPayment(due: IDuePayment) {
        const left = due.saldo - due.paid;
        due.settled = false;
        // due.remainingPayment = due.remainingPayment - due.paid;
        if (left === 0) {
            due.settled = true;
        }
        due.remainingPayment = left;
        // console.log(due);
    }

    protected paginateDuePayments(data: ISaleTransactions[], headers: HttpHeaders) {
        this.sales = [];
        this.duePayments = [];
        this.totalPaid = 0;
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.sales.push(data[i]);
            const duePayment: IDuePayment = this.createDuePayment(data[i]);
            // console.log(duePayment);
            this.duePayments.push(duePayment);
        }
    }

    protected createDuePayment(sale: ISaleTransactions): IDuePayment {
        this.totalNominal = 0;
        this.totalSaldo = 0;
        // tslint:disable-next-line: prefer-const
        let duePayment = new DuePayment();
        duePayment.customerLastName = sale.customerLastName;
        duePayment.createdDate = sale.saleDate;
        duePayment.creatorId = this.currentAccount.id;
        duePayment.creatorLogin = this.currentAccount.login;
        duePayment.customerId = sale.customerId;
        duePayment.customerFirstName = sale.customerFirstName;
        duePayment.customerFullName = sale.customerFirstName + ' ' + sale.customerLastName;
        duePayment.paid = 0;
        duePayment.remainingPayment = sale.remainingPayment;
        duePayment.saldo = duePayment.remainingPayment;
        duePayment.saleId = sale.id;
        duePayment.saleNoInvoice = sale.noInvoice;
        duePayment.totalPayment = sale.totalPayment;
        duePayment.isEdit = false;

        this.totalNominal += duePayment.totalPayment;
        this.totalSaldo += duePayment.remainingPayment;

        return duePayment;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
