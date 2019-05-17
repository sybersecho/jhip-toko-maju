import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBadStockProduct } from 'app/shared/model/bad-stock-product.model';
import { BadStockProductService } from './bad-stock-product.service';

@Component({
    selector: 'jhi-bad-stock-product-delete-dialog',
    templateUrl: './bad-stock-product-delete-dialog.component.html'
})
export class BadStockProductDeleteDialogComponent {
    badStockProduct: IBadStockProduct;

    constructor(
        protected badStockProductService: BadStockProductService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.badStockProductService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'badStockProductListModification',
                content: 'Deleted an badStockProduct'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bad-stock-product-delete-popup',
    template: ''
})
export class BadStockProductDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ badStockProduct }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BadStockProductDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.badStockProduct = badStockProduct;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/bad-stock-product', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/bad-stock-product', { outlets: { popup: null } }]);
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
