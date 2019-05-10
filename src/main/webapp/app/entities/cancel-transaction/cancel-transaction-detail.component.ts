import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICancelTransaction } from 'app/shared/model/cancel-transaction.model';

@Component({
    selector: 'jhi-cancel-transaction-detail',
    templateUrl: './cancel-transaction-detail.component.html'
})
export class CancelTransactionDetailComponent implements OnInit {
    cancelTransaction: ICancelTransaction;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cancelTransaction }) => {
            this.cancelTransaction = cancelTransaction;
        });
    }

    previousState() {
        window.history.back();
    }
}
