import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDuePayment } from 'app/shared/model/due-payment.model';

@Component({
    selector: 'jhi-due-payment-detail',
    templateUrl: './due-payment-detail.component.html'
})
export class DuePaymentDetailComponent implements OnInit {
    duePayment: IDuePayment;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ duePayment }) => {
            this.duePayment = duePayment;
        });
    }

    previousState() {
        window.history.back();
    }
}
