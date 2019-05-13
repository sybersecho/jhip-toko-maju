import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReturnTransaction } from 'app/shared/model/return-transaction.model';

@Component({
    selector: 'jhi-return-transaction-detail',
    templateUrl: './return-transaction-detail.component.html'
})
export class ReturnTransactionDetailComponent implements OnInit {
    returnTransaction: IReturnTransaction;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ returnTransaction }) => {
            this.returnTransaction = returnTransaction;
        });
    }

    previousState() {
        window.history.back();
    }
}
