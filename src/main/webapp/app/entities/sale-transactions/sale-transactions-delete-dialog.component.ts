import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from './sale-transactions.service';

@Component({
    selector: 'jhi-sale-transactions-delete-dialog',
    templateUrl: './sale-transactions-delete-dialog.component.html'
})
export class SaleTransactionsDeleteDialogComponent {
    saleTransactions: ISaleTransactions;

    constructor(
        protected saleTransactionsService: SaleTransactionsService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.saleTransactionsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'saleTransactionsListModification',
                content: 'Deleted an saleTransactions'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sale-transactions-delete-popup',
    template: ''
})
export class SaleTransactionsDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ saleTransactions }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SaleTransactionsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.saleTransactions = saleTransactions;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sale-transactions', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sale-transactions', { outlets: { popup: null } }]);
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
