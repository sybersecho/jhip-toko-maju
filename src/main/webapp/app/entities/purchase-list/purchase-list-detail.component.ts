import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseList } from 'app/shared/model/purchase-list.model';

@Component({
    selector: 'jhi-purchase-list-detail',
    templateUrl: './purchase-list-detail.component.html'
})
export class PurchaseListDetailComponent implements OnInit {
    purchaseList: IPurchaseList;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchaseList }) => {
            this.purchaseList = purchaseList;
        });
    }

    previousState() {
        window.history.back();
    }
}
