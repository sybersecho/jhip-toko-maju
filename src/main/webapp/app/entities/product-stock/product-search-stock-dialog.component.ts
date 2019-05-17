import { Component, OnInit, Injectable } from '@angular/core';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from '../product';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';

@Component({
    selector: 'jhi-product-search-stock-dialog',
    templateUrl: './product-search-stock-dialog.component.html',
    styles: []
})
export class ProductSearchStockDialogComponent implements OnInit {
    products: IProduct[];
    currentSearch: any;

    constructor(
        protected productService: ProductService,
        protected eventManager: JhiEventManager,
        protected jhiAlertService: JhiAlertService,
        public activeModal: NgbActiveModal
    ) {}

    ngOnInit() {
        // this.loadAll();
    }

    addProduct(product: IProduct) {
        this.eventManager.broadcast({ name: 'addProductEvent', content: product });
    }

    loadAll() {
        if (this.currentSearch) {
            console.log('search by: ' + this.currentSearch);
            this.productService
                .searchBy({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IProduct[]>) => this.paginateProducts(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.products = [];
        // this.productService
        //     .query()
        //     .subscribe(
        //         (res: HttpResponse<IProduct[]>) => this.paginateProducts(res.body, res.headers),
        //         (res: HttpErrorResponse) => this.onError(res.message)
        //     );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    protected paginateProducts(data: IProduct[], headers: HttpHeaders) {
        this.products = data;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

@Injectable({
    providedIn: 'root'
})
export class SearchProductStockDialogService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }

        this.isOpen = true;
        const modalRef = this.modalService.open(ProductSearchStockDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        modalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return modalRef;
        // }, 100);
    }
}
