import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchase } from 'app/shared/model/purchase.model';

@Component({
    selector: 'jhi-purchase-detail',
    templateUrl: './purchase-detail.component.html'
})
export class PurchaseDetailComponent implements OnInit {
    purchase: IPurchase;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchase }) => {
            this.purchase = purchase;
        });
    }

    previousState() {
        window.history.back();
    }
}
