import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICancelTransaction } from 'app/shared/model/cancel-transaction.model';
import { CancelTransactionService } from './cancel-transaction.service';

@Component({
    selector: 'jhi-cancel-transaction-delete-dialog',
    templateUrl: './cancel-transaction-delete-dialog.component.html'
})
export class CancelTransactionDeleteDialogComponent {
    cancelTransaction: ICancelTransaction;

    constructor(
        protected cancelTransactionService: CancelTransactionService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cancelTransactionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cancelTransactionListModification',
                content: 'Deleted an cancelTransaction'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cancel-transaction-delete-popup',
    template: ''
})
export class CancelTransactionDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cancelTransaction }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CancelTransactionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.cancelTransaction = cancelTransaction;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/cancel-transaction', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/cancel-transaction', { outlets: { popup: null } }]);
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
