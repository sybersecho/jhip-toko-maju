import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModalRef, NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { IProduct } from 'app/shared/model/product.model';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ProductService } from 'app/entities/product';

@Component({
    selector: 'jhi-search-product-dialog',
    templateUrl: './search-product-dialog.component.html',
    styles: []
})
export class SearchProductDialogComponent {
    products: IProduct[];
    searchKeyword: string;

    constructor(
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected productService: ProductService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    clearSearch() {
        this.searchKeyword = '';
        this.products = null;
        // this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }

        this.searchKeyword = query;

        this.loadAll();
    }

    loadAll(): void {
        if (this.searchKeyword) {
            console.log('search by: ' + this.searchKeyword);
            this.productService
                .searchBy({
                    // page: this.page - 1,
                    query: this.searchKeyword
                    // size: this.itemsPerPage,
                    // sort: this.sort()
                })
                .subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body));
            return;
        }

        this.productService
            .query({
                // page: this.page - 1,
                // size: this.itemsPerPage,
                // sort: this.sort()
            })
            .subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body));
    }

    onSelect(product: IProduct) {
        this.eventManager.broadcast({
            name: 'onSelectProductEvent',
            data: product
        });
        this.activeModal.dismiss(true);
    }
}

@Component({
    selector: 'jhi-search-product-popup',
    template: ''
})
export class SearchProductPopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}
    ngOnInit(): void {
        this.activatedRoute.data.subscribe(({ products }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SearchProductDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.products = products;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
