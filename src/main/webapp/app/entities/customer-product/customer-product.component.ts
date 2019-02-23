import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { AccountService } from 'app/core';
import { CustomerProductService } from './customer-product.service';

@Component({
    selector: 'jhi-customer-product',
    templateUrl: './customer-product.component.html'
})
export class CustomerProductComponent implements OnInit, OnDestroy {
    customerProducts: ICustomerProduct[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected customerProductService: CustomerProductService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.customerProductService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<ICustomerProduct[]>) => res.ok),
                    map((res: HttpResponse<ICustomerProduct[]>) => res.body)
                )
                .subscribe(
                    (res: ICustomerProduct[]) => (this.customerProducts = res),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.customerProductService
            .query()
            .pipe(
                filter((res: HttpResponse<ICustomerProduct[]>) => res.ok),
                map((res: HttpResponse<ICustomerProduct[]>) => res.body)
            )
            .subscribe(
                (res: ICustomerProduct[]) => {
                    this.customerProducts = res;
                    this.currentSearch = '';
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    searchByCustomer() {
        console.log('searchByCustomer');
        this.customerProductService
        .findByCustomer()
        .pipe(
            filter((res: HttpResponse<ICustomerProduct[]>) => res.ok),
            map((res: HttpResponse<ICustomerProduct[]>) => res.body)
        )
        .subscribe(
            (res: ICustomerProduct[]) => (this.customerProducts = res),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    clear() {
        console.log('clear');
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInCustomerProducts();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICustomerProduct) {
        return item.id;
    }

    registerChangeInCustomerProducts() {
        this.eventSubscriber = this.eventManager.subscribe('customerProductListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
