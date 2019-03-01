import { Component, OnInit, OnDestroy, OnChanges } from '@angular/core';
import { IProduct } from 'app/shared/model/product.model';
import { Subscription } from 'rxjs';
import { ProductService } from 'app/entities/product';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { ActivatedRoute, Router, Data } from '@angular/router';
import { ITEMS_PER_PAGE } from 'app/shared';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { ICustomerProduct, CustomerProduct } from 'app/shared/model/customer-product.model';
import { CustomerProductService } from '../customer-product';
import { ICustomer } from 'app/shared/model/customer.model';

@Component({
    selector: 'jhi-search-product',
    templateUrl: './search-product.component.html',
    styles: []
})
export class SearchProductComponent implements OnInit, OnDestroy {
    currentAccount: any;
    products: IProduct[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    searchByProduct: string;
    routeData: any;
    links: any;
    totalItems: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    customerId: number;
    customerProduct: ICustomerProduct;

    constructor(
        protected productService: ProductService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected eventManager: JhiEventManager,
        protected customerProductService: CustomerProductService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });

        this.activatedRoute.data.subscribe((data: Data) => {
            // console.log('activated route call');
            // console.log(data);
            const customer: ICustomer = data['customer'];
            // console.log(customer);
            this.customerId = customer.id;
        });

        this.searchByProduct =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['searchByProduct']
                ? this.activatedRoute.snapshot.params['searchByProduct']
                : '';
        this.customerProduct = new CustomerProduct();
        this.customerProduct.customerId = this.customerId;
    }

    // ngOnChanges() {}

    ngOnInit() {
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.loadAll();
        // this.registerChangeInProducts();
    }

    // registerChangeInProducts() {
    //     this.eventSubscriber = this.eventManager.subscribe('productListModification', response => this.loadAll());
    // }

    ngOnDestroy() {
        // this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IProduct) {
        return item.id;
    }

    loadAll() {
        if (this.searchByProduct) {
            this.productService
                .search({
                    page: this.page - 1,
                    query: this.searchByProduct,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IProduct[]>) => this.paginateProducts(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );

            return;
        }
        // this.productService
        //     .query({
        //         page: this.page - 1,
        //         size: this.itemsPerPage,
        //         sort: this.sort()
        //     })
        //     .subscribe(
        //         (res: HttpResponse<IProduct[]>) => this.paginateProducts(res.body, res.headers),
        //         (res: HttpErrorResponse) => this.onError(res.message)
        //     );
    }

    clear() {
        this.page = 0;
        console.log('clear');
        this.products = null;
        // this.currentSearch = '';
        // this.router.navigate([
        //     '/product',
        //     {
        //         page: this.page,
        //         sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //     }
        // ]);
        this.loadAll();
    }

    private search(query) {
        if (!query) {
            return this.clear();
        }
        console.log(query);
        this.page = 0;
        if (this.searchByProduct) {
            console.log('search by product wiht query: ' + this.searchByProduct);
        }
        // this.currentSearch = query;
        // this.router.navigate([
        //     '/product',
        //     {
        //         // search: this.currentSearch,
        //         page: this.page,
        //         sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        //     }
        // ]);
        // this.loadAll();
    }

    onAddCustomerProduct(product: IProduct) {
        this.customerProduct.productId = product.id;
        this.customerProduct.specialPrice = product.sellingPrices;
        console.log('customer ID ' + this.customerProduct.customerId);
        console.log('active id: ' + this.activatedRoute.params['id']);
        this.customerProductService.create(this.customerProduct).subscribe(
            res => {
                console.log(res);
            },
            err => {
                console.log(err);
            }
        );
        console.log('addProduct' + this.customerProduct);
    }

    searchByProductQuery(query) {
        // this.searchBySuppler = '' TODO
        // this.search(query);
        this.page = 0;
        this.searchByProduct = query;
        this.router.navigate(
            [
                '/customer',
                this.customerId,
                'add-products',
                {
                    searchByProduct: this.searchByProduct,
                    page: this.page,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
            ],
            { relativeTo: this.activatedRoute }
        );
        this.loadAll();
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    searchBySupplierQuery(query) {}

    clearProductSearch() {
        this.searchByProduct = '';
        this.products = null;
        this.router.navigate(['/customer', this.customerId, 'add-products'], { relativeTo: this.activatedRoute });
    }

    clearSupplierSearch() {}

    protected paginateProducts(data: IProduct[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.products = data;
        console.log(this.products);
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    protected createNameOrBarcodeParams(query: string): string {
        const barcode = 'barcode.contains=' + query;
        const name = 'name.contains=' + query;
        const params = barcode.concat('&').concat(name);
        console.log(params);
        return params;
    }
}
