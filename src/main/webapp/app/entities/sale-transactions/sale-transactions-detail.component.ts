import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';

@Component({
    selector: 'jhi-sale-transactions-detail',
    templateUrl: './sale-transactions-detail.component.html'
})
export class SaleTransactionsDetailComponent implements OnInit {
    saleTransactions: ISaleTransactions;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ saleTransactions }) => {
            this.saleTransactions = saleTransactions;
        });
    }

    previousState() {
        window.history.back();
    }
}
