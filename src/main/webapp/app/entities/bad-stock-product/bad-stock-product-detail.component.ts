import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBadStockProduct } from 'app/shared/model/bad-stock-product.model';

@Component({
    selector: 'jhi-bad-stock-product-detail',
    templateUrl: './bad-stock-product-detail.component.html'
})
export class BadStockProductDetailComponent implements OnInit {
    badStockProduct: IBadStockProduct;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ badStockProduct }) => {
            this.badStockProduct = badStockProduct;
        });
    }

    previousState() {
        window.history.back();
    }
}
