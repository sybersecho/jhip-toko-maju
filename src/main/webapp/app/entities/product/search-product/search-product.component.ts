import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { CustomerService } from 'app/entities/customer/customer.service';
import { ICustomerProduct, CustomerProduct } from 'app/shared/model/customer-product.model';
import { CustomerProductService } from 'app/entities/customer/customer-product/customer-product.service';
import { Observable } from 'rxjs';
import { ICustomer } from 'app/shared/model/customer.model';
import { ProductService } from 'app/entities/product';
import { IProduct } from 'app/shared/model/product.model';

@Component({
    selector: 'jhi-search-product',
    templateUrl: './search-product.component.html',
    styles: []
})
export class SearchProductComponent implements OnInit {
    currentAccount: any;
    customer: ICustomer;
    entity: any;
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
        this.activatedRoute.data.subscribe(({ entity }) => {
            // this.customer = customer;
            this.entity = entity;
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

    back() {
        // this.router.navigate(['customer', this.customer.id, 'products']);
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
        // console.log('save id: ' + product.id);
        // this.createCustomerProduct(product);
        // this.subscribeToSaveResponse(this.customerProductService.create(this.save));
        this.eventManager.broadcast({ name: 'addProduct', content: product, entity: this.entity });
        // console.log(this.save);
    }

    protected onSaveError() {
        // this.isSaving = false;
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
