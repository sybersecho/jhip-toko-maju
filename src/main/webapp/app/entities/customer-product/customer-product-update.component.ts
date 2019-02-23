import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { CustomerProductService } from './customer-product.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
    selector: 'jhi-customer-product-update',
    templateUrl: './customer-product-update.component.html'
})
export class CustomerProductUpdateComponent implements OnInit {
    customerProduct: ICustomerProduct;
    isSaving: boolean;

    products: IProduct[];

    customers: ICustomer[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected customerProductService: CustomerProductService,
        protected productService: ProductService,
        protected customerService: CustomerService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ customerProduct }) => {
            this.customerProduct = customerProduct;
        });
        this.productService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IProduct[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProduct[]>) => response.body)
            )
            .subscribe((res: IProduct[]) => (this.products = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.customerService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICustomer[]>) => response.body)
            )
            .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.customerProduct.id !== undefined) {
            this.subscribeToSaveResponse(this.customerProductService.update(this.customerProduct));
        } else {
            this.subscribeToSaveResponse(this.customerProductService.create(this.customerProduct));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerProduct>>) {
        result.subscribe((res: HttpResponse<ICustomerProduct>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackProductById(index: number, item: IProduct) {
        return item.id;
    }

    trackCustomerById(index: number, item: ICustomer) {
        return item.id;
    }
}
