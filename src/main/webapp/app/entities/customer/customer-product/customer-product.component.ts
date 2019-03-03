import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../customer.service';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Router, ActivatedRoute, Data } from '@angular/router';
import { AccountService } from 'app/core';

@Component({
    selector: 'jhi-customer-product',
    templateUrl: './customer-product.component.html',
    styles: []
})
export class CustomerProductComponent implements OnInit {
    customerProducts: ICustomerProduct[] = null;
    constructor(
        protected customerService: CustomerService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected router: Router,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {}

    ngOnInit() {
        this.loadAll();
    }

    loadAll() {
        this.activatedRoute.data.subscribe((data: Data) => {
            this.customerProducts = data['customerProducts'];
            console.log(this.customerProducts);
        });
    }

    getCustomerProduct() {
        this.customerService.searcyByCustomer(1).subscribe(
            res => {
                console.log(res);
            },
            err => {
                console.log(err);
            }
        );
    }

    trackId(index: number, item: ICustomerProduct) {
        return item.id;
    }
}
