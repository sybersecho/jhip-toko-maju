import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPurchase } from 'app/shared/model/purchase.model';
import { PurchaseService } from './purchase.service';

@Component({
    selector: 'jhi-purchase-delete-dialog',
    templateUrl: './purchase-delete-dialog.component.html'
})
export class PurchaseDeleteDialogComponent {
    purchase: IPurchase;

    constructor(protected purchaseService: PurchaseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.purchaseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'purchaseListModification',
                content: 'Deleted an purchase'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-purchase-delete-popup',
    template: ''
})
export class PurchaseDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ purchase }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PurchaseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.purchase = purchase;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/purchase', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/purchase', { outlets: { popup: null } }]);
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
