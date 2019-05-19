import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchaseList } from 'app/shared/model/purchase-list.model';
import { PurchaseListService } from './purchase-list.service';

@Component({
    selector: 'jhi-purchase-list-delete-dialog',
    templateUrl: './purchase-list-delete-dialog.component.html'
})
export class PurchaseListDeleteDialogComponent {
    purchaseList: IPurchaseList;

    constructor(
        protected purchaseListService: PurchaseListService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.purchaseListService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'purchaseListListModification',
                content: 'Deleted an purchaseList'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-purchase-list-delete-popup',
    template: ''
})
export class PurchaseListDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchaseList }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PurchaseListDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.purchaseList = purchaseList;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/purchase-list', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/purchase-list', { outlets: { popup: null } }]);
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
