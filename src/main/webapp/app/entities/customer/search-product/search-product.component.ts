import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-search-product',
    templateUrl: './search-product.component.html',
    styles: []
})
export class SearchProductComponent implements OnInit {
    products: IProduct[];
    currentSearch: any;
    predicate: any;
    reverse: any;
    constructor(
        protected productService: ProductService,
        protected eventManager: JhiEventManager,
        protected jhiAlertService: JhiAlertService,
        public activeModal: NgbActiveModal
    ) {}

    ngOnInit() {
        this.loadAll();
    }

    addProduct(product: IProduct) {
        // console.log('save id: ' + product.id);
        // this.createCustomerProduct(product);
        // this.subscribeToSaveResponse(this.customerProductService.create(this.save));
        this.eventManager.broadcast({ name: 'customerProductEvent', content: product });
        // console.log(this.save);
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

    protected paginateProducts(data: IProduct[], headers: HttpHeaders) {
        // this.links = this.parseLinks.parse(headers.get('link'));
        // this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.products = data;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
