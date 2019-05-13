import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReturnTransaction } from 'app/shared/model/return-transaction.model';
import { ReturnTransactionService } from './return-transaction.service';

@Component({
    selector: 'jhi-return-transaction-delete-dialog',
    templateUrl: './return-transaction-delete-dialog.component.html'
})
export class ReturnTransactionDeleteDialogComponent {
    returnTransaction: IReturnTransaction;

    constructor(
        protected returnTransactionService: ReturnTransactionService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.returnTransactionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'returnTransactionListModification',
                content: 'Deleted an returnTransaction'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-return-transaction-delete-popup',
    template: ''
})
export class ReturnTransactionDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ returnTransaction }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ReturnTransactionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.returnTransaction = returnTransaction;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/return-transaction', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/return-transaction', { outlets: { popup: null } }]);
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
