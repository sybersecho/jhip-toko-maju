import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerProduct } from 'app/shared/model/customer-product.model';

@Component({
    selector: 'jhi-customer-product-detail',
    templateUrl: './customer-product-detail.component.html'
})
export class CustomerProductDetailComponent implements OnInit {
    customerProduct: ICustomerProduct;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customerProduct }) => {
            this.customerProduct = customerProduct;
        });
    }

    previousState() {
        window.history.back();
    }
}
