import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { CustomerService } from '../customer.service';
import { ICustomerProduct, CustomerProduct } from 'app/shared/model/customer-product.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Router, ActivatedRoute, Data } from '@angular/router';
import { AccountService } from 'app/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerProductService } from './customer-product.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SearchProductModalService } from '../search-product/search-product-modal.service';
import { IProduct } from 'app/shared/model/product.model';
// import { SearchProductModalService } from '../../customer';

@Component({
    selector: 'jhi-customer-product',
    templateUrl: './customer-product.component.html',
    styles: []
})
export class CustomerProductComponent implements OnInit, OnDestroy {
    customerProducts: ICustomerProduct[];
    @Input() customer: ICustomer;
    modalRef: NgbModalRef;
    eventSubscription: Subscription;

    constructor(
        protected customerService: CustomerService,
        protected customerProductService: CustomerProductService,
        protected searchProductModalService: SearchProductModalService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected router: Router,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {}

    ngOnInit() {
        // this.loadAll();
        this.reloadCustomerProduct();
        this.registerEvent();
    }

    protected registerEvent() {
        this.eventSubscription = this.eventManager.subscribe('customerProductEvent', response => this.addCustomerProduct(response.content));
    }

    protected addCustomerProduct(product: IProduct) {
        const newProduct: ICustomerProduct = this.createNewCustomerProduct(product);
        this.subscribeToSaveResponse(this.customerProductService.create(newProduct));
        this.reloadCustomerProduct();
    }

    protected createNewCustomerProduct(product: IProduct): ICustomerProduct {
        const newCustomerProduct = new CustomerProduct();
        newCustomerProduct.productId = product.id;
        newCustomerProduct.specialPrice = product.sellingPrice;
        newCustomerProduct.customerId = this.customer.id;

        return newCustomerProduct;
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubscription);
        this.modalRef = null;
    }

    loadAll() {
        this.reloadCustomerProduct();
        // this.activatedRoute.data.subscribe((data: Data) => {
        //     this.customerProducts = data['customerProducts'];
        //     this.customer = data['customer'];
        // });
    }

    searchProduct() {
        console.log('search product');
        this.modalRef = this.searchProductModalService.open();
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
