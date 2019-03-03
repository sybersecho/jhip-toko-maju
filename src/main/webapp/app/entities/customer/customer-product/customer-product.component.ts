import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../customer.service';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Router, ActivatedRoute, Data } from '@angular/router';
import { AccountService } from 'app/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerProductService } from './customer-product.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
    selector: 'jhi-customer-product',
    templateUrl: './customer-product.component.html',
    styles: []
})
export class CustomerProductComponent implements OnInit {
    customerProducts: ICustomerProduct[];
    customer: ICustomer;

    constructor(
        protected customerService: CustomerService,
        protected customerProductService: CustomerProductService,
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
            this.customer = data['customer'];
        });
    }

    update(product: ICustomerProduct) {
        this.subscribeToSaveResponse(this.customerProductService.update(product));
    }

    delete(product: ICustomerProduct) {
        this.subscribeToSaveResponse(this.customerProductService.delete(product.id));
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerProduct>>) {
        result.subscribe((res: HttpResponse<ICustomer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        // this.isSaving = false;
        // this.previousState();
        // this.jhiAlertService.success('jhiptokomajuApp.product.home.title', null, 'top left');
        this.reloadCustomerProduct();
    }

    protected onSaveError() {
        // this.isSaving = false;
    }

    protected reloadCustomerProduct() {
        this.customerService.searcyByCustomer(this.customer.id).subscribe(
            res => {
                this.customerProducts = res.body;
            },
            err => this.onError(err.errorMessage)
        );
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    saveAll() {
        this.customerProductService.batchUpdate(this.customerProducts).subscribe(
            res => {
                this.reloadCustomerProduct();
            },
            err => {
                this.onError(err.errorMessage);
            }
        );
    }

    // getCustomerProduct() {
    //     this.customerService.searcyByCustomer(1).subscribe(
    //         res => {
    //             console.log(res);
    //         },
    //         err => {
    //             console.log(err);
    //         }
    //     );
    // }

    trackId(index: number, item: ICustomerProduct) {
        return item.id;
    }
}
