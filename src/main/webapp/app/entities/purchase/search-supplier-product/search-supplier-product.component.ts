import { Component, OnInit, Injectable } from '@angular/core';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ISupplier } from 'app/shared/model/supplier.model';

@Component({
    selector: 'jhi-search-supplier-product',
    templateUrl: './search-supplier-product.component.html',
    styles: []
})
export class SearchSupplierProductComponent implements OnInit {
    products: IProduct[];
    currentSearch: any;
    predicate: any;
    reverse: any;
    supplier: ISupplier;

    constructor(
        protected productService: ProductService,
        protected eventManager: JhiEventManager,
        protected jhiAlertService: JhiAlertService,
        public activeModal: NgbActiveModal
    ) {}

    ngOnInit() {}

    addProduct(product: IProduct) {
        this.eventManager.broadcast({ name: 'addProductEvent', content: product });
    }

    loadAll() {
        if (this.currentSearch) {
            this.productService
                .findProductsOfSupplierContains(this.supplier.id, this.currentSearch)
                .subscribe(
                    (res: HttpResponse<IProduct[]>) => this.paginateProducts(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.products = [];
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
export class SearchSupplierProductService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    open(supplir: ISupplier): NgbModalRef {
        if (this.isOpen) {
            return;
        }

        this.isOpen = true;
        const modalRef = this.modalService.open(SearchSupplierProductComponent as Component, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.supplier = supplir;
        modalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return modalRef;
    }
}
