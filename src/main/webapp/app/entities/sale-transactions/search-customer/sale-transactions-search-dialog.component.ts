import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, Data } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from '../sale-transactions.service';
import { ICustomer } from 'app/shared/model/customer.model';
// import { SaleTransactionsService } from './sale-transactions.service';

@Component({
    selector: 'jhi-sale-transactions-searc-dialog',
    templateUrl: './sale-transactions-search-dialog.component.html'
})
export class SaleTransactionsSearchDialogComponent {
    saleTransactions: ISaleTransactions;
    customers: ICustomer[];

    constructor(
        protected saleTransactionsService: SaleTransactionsService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected router: Router
    ) {
        // this.activatedRoute.data.subscribe((data: Data) => {
        //     // this.customerProducts = data['customerProducts'];
        //     this.customers = data['customers'];
        // });
    }

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

    onSelect(customer: ICustomer) {
        this.eventManager.broadcast({
            name: 'onSelectCustomerEvent',
            data: customer
        });
        this.activeModal.dismiss(true);
    }
}

@Component({
    selector: 'jhi-sale-transactions-search-popup',
    template: ''
})
export class SaleTransactionsSearchPopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ customers }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SaleTransactionsSearchDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.customers = customers;
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
