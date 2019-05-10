import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ICancelTransaction, CancelTransaction } from 'app/shared/model/cancel-transaction.model';
import { AccountService } from 'app/core';

import { CancelTransactionService } from './cancel-transaction.service';
import { ISaleItem } from 'app/shared/model/sale-item.model';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from '../sale-transactions';
import { Observable } from 'rxjs';
import moment = require('moment');

@Component({
    selector: 'jhi-cancel-transaction',
    templateUrl: './cancel-transaction.component.html'
})
export class CancelTransactionComponent implements OnInit, OnDestroy {
    cancelTransactions: ICancelTransaction[];
    currentAccount: any;
    currentSearch: string;
    items: ISaleItem[];
    sale: ISaleTransactions;

    note: string;

    constructor(
        protected cancelTransactionService: CancelTransactionService,
        protected saleService: SaleTransactionsService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        // this.currentSearch =
        //     this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
        //         ? this.activatedRoute.snapshot.params['search']
        //         : '';

        this.items = [];
    }

    clear() {
        this.items = [];
        this.currentSearch = '';
        this.sale = null;
    }

    search(query) {
        if (!query) {
            this.items = [];
            this.sale = null;
            return;
        }
        this.saleService.findPaidInvoice(query).subscribe(
            res => {
                this.sale = res.body[0];
                if (!this.sale) {
                    this.onError('error.search.not.found');
                    this.items = [];
                    this.sale = null;
                    return;
                }

                this.sale.projectName = this.sale.projectId ? this.sale.projectName : '-';
                this.paginateItemsTransaction(this.sale.items);
            },
            error => {
                console.error(error.message);
                this.onError('error.somethingwrong');
            }
        );
    }

    ngOnInit() {}

    ngOnDestroy() {}

    save() {
        const cancelSale: ICancelTransaction = new CancelTransaction();
        cancelSale.noInvoice = this.sale.noInvoice;
        cancelSale.saleTransactionsId = this.sale.id;
        cancelSale.saleTransactionsNoInvoice = this.sale.noInvoice;
        cancelSale.note = this.note ? this.note : '-';
        cancelSale.cancelDate = moment();
        this.subscribeToSaveResponse(this.cancelTransactionService.create(cancelSale));
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICancelTransaction>>) {
        result.subscribe(
            (res: HttpResponse<ICancelTransaction>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    saveAndPrint() {}

    protected onSaveSuccess(cancel: ICancelTransaction) {
        // this.jhiAlertService.success('jhiptokomajuApp.cancelTransaction.created', cancel.noInvoice);
        this.sale = null;
        this.note = '';
        this.items = [];
    }

    protected paginateItemsTransaction(data: ISaleItem[]) {
        this.items = [];
        data.forEach(e => {
            this.items.push(e);
        });
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
