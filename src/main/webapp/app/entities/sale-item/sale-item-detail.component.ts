import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISaleItem } from 'app/shared/model/sale-item.model';

@Component({
    selector: 'jhi-sale-item-detail',
    templateUrl: './sale-item-detail.component.html'
})
export class SaleItemDetailComponent implements OnInit {
    saleItem: ISaleItem;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ saleItem }) => {
            this.saleItem = saleItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
