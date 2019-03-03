import { Component, OnInit } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from 'app/entities/product';
import { IProduct } from 'app/shared/model/product.model';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { CustomerService } from '../customer.service';
import { ICustomerProduct, CustomerProduct } from 'app/shared/model/customer-product.model';
import { CustomerProductService } from '../customer-product/customer-product.service';
import { Observable } from 'rxjs';

@Component({
    selector: 'jhi-search-product',
    templateUrl: './search-product.component.html',
    styles: []
})
export class SearchProductComponent implements OnInit {
    currentAccount: any;
    customer: ICustomer;
    products: IProduct[];
    save: ICustomerProduct;
    error: any;
    success: any;
    predicate: any;
    reverse: any;
    currentSearch: any;

    constructor(
        protected productService: ProductService,
        protected customerSevice: CustomerService,
        protected customerProductService: CustomerProductService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.activatedRoute.data.subscribe(({ customer }) => {
            this.customer = customer;
        });
    }

    loadAll() {
        if (this.currentSearch) {
            console.log('search by: ' + this.currentSearch);
            this.productService
                .searchBy({
                    // page: this.page - 1,
                    query: this.currentSearch
                    // size: this.itemsPerPage,
                    // sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IProduct[]>) => this.paginateProducts(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.productService
            .query({
                // page: this.page - 1,
                // size: this.itemsPerPage,
                // sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IProduct[]>) => this.paginateProducts(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    clear() {
        // this.page = 0;
        this.currentSearch = '';
        // this.router.navigate([
        //     '/product',
        //     {
        //         page: this.page,
        //         sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //     }
        // ]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        // this.page = 0;
        this.currentSearch = query;
        // this.router.navigate([
        //     '/product',
        //     {
        //         search: this.currentSearch,
        //         page: this.page,
        //         sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //     }
        // ]);
        this.loadAll();
    }

    addProduct(product: IProduct) {
        console.log('save id: ' + product.id);
        this.createCustomerProduct(product);
        this.subscribeToSaveResponse(this.customerProductService.create(this.save));
        // console.log(this.save);
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerProduct>>) {
        result.subscribe((res: HttpResponse<ICustomer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onError(res.message));
    }

    protected onSaveSuccess() {
        // this.isSaving = false;
        // this.previousState();
    }

    protected onSaveError() {
        // this.isSaving = false;
    }

    protected createCustomerProduct(product: IProduct) {
        this.save = new CustomerProduct();
        this.save.productId = product.id;
        this.save.specialPrice = product.sellingPrice;
        this.save.customerId = this.customer.id;
    }

    protected paginateProducts(data: IProduct[], headers: HttpHeaders) {
        // this.links = this.parseLinks.parse(headers.get('link'));
        // this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.products = data;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
