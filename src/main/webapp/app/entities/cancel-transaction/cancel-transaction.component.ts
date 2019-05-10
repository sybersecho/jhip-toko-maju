import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ICancelTransaction } from 'app/shared/model/cancel-transaction.model';
import { AccountService } from 'app/core';

import { CancelTransactionService } from './cancel-transaction.service';
import { ISaleItem } from 'app/shared/model/sale-item.model';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from '../sale-transactions';

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

    note: String;

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
        this.saleService.findByInvoice(query).subscribe(
            res => {
                if (!res.body[0]) {
                    this.onError('error.search.not.found');
                    this.items = [];
                    this.sale = null;
                    return;
                }
                this.sale = res.body[0];
                this.sale.projectName = this.sale.projectId ? this.sale.projectName : '-';
                this.paginateItemsTransaction(this.sale.items);
            },
            error => {
                this.onError('error.somethingwrong');
            }
        );
    }

    ngOnInit() {}

    ngOnDestroy() {
        // this.eventManager.destroy(this.eventSubscriber);
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
