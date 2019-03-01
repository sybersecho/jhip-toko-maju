import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, Router } from '@angular/router';
import { Subscription, Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { AccountService } from 'app/core';
import { CustomerProductService } from './customer-product.service';
import { NgForm } from '@angular/forms';
import { ICustomer } from 'app/shared/model/customer.model';

@Component({
    selector: 'jhi-customer-product',
    templateUrl: './customer-product.component.html'
})
export class CustomerProductComponent implements OnInit, OnDestroy {
    customerProducts: ICustomerProduct[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    customer: ICustomer;

    constructor(
        protected customerProductService: CustomerProductService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected router: Router,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.activatedRoute.data.subscribe((data: Data) => {
            this.customerProducts = data['customerProducts'];
            this.customer = data['customer'];
        });
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        console.log('clear');
        this.currentSearch = '';
        this.reload();
    }

    ngOnInit() {
        this.loadAll();
    }

    ngOnDestroy() {
        // this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ICustomerProduct) {
        return item.id;
    }

    registerChangeInCustomerProducts() {
        // this.eventSubscriber = this.eventManager.subscribe('customerProductListModification', response => this.reload);
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSaveAll() {
        this.subscribeToSaveResponse(this.customerProductService.saveOrUpdate(this.customerProducts));
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerProduct>>) {
        result.subscribe((res: HttpResponse<ICustomerProduct>) => this.onSaveSuccess(res), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess(res: HttpResponse<ICustomerProduct>) {
        this.previousState();
    }

    protected onSaveError() {}

    previousState() {
        this.router.navigate(['/customer']);
    }

    deleteCustomerProduct(id: number) {
        this.customerProductService.delete(id).subscribe(
            res => {
                this.reload();
            },
            err => {
                console.log('error');
            }
        );
    }

    saveCustomerProduct(customerProduct: ICustomerProduct) {
        this.customerProductService.update(customerProduct).subscribe(
            res => {
                this.reload();
            },
            err => {
                console.log(err);
            }
        );
    }

    protected reload() {
        this.customerProductService
            .findByCustomer(this.customer.id)
            .subscribe((res: HttpResponse<ICustomerProduct[]>) => (this.customerProducts = res.body));
    }
}
