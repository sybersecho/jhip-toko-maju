import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { CustomerProductService } from './customer-product.service';

@Component({
    selector: 'jhi-customer-product-delete-dialog',
    templateUrl: './customer-product-delete-dialog.component.html'
})
export class CustomerProductDeleteDialogComponent {
    customerProduct: ICustomerProduct;

    constructor(
        protected customerProductService: CustomerProductService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.customerProductService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'customerProductListModification',
                content: 'Deleted an customerProduct'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-customer-product-delete-popup',
    template: ''
})
export class CustomerProductDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customerProduct }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CustomerProductDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.customerProduct = customerProduct;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/customer-product', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/customer-product', { outlets: { popup: null } }]);
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
