import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDuePayment } from 'app/shared/model/due-payment.model';
import { DuePaymentService } from './due-payment.service';

@Component({
    selector: 'jhi-due-payment-delete-dialog',
    templateUrl: './due-payment-delete-dialog.component.html'
})
export class DuePaymentDeleteDialogComponent {
    duePayment: IDuePayment;

    constructor(
        protected duePaymentService: DuePaymentService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.duePaymentService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'duePaymentListModification',
                content: 'Deleted an duePayment'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-due-payment-delete-popup',
    template: ''
})
export class DuePaymentDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ duePayment }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DuePaymentDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.duePayment = duePayment;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/due-payment', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/due-payment', { outlets: { popup: null } }]);
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
