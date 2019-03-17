import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISaleItem } from 'app/shared/model/sale-item.model';
import { SaleItemService } from './sale-item.service';

@Component({
    selector: 'jhi-sale-item-delete-dialog',
    templateUrl: './sale-item-delete-dialog.component.html'
})
export class SaleItemDeleteDialogComponent {
    saleItem: ISaleItem;

    constructor(protected saleItemService: SaleItemService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.saleItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'saleItemListModification',
                content: 'Deleted an saleItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sale-item-delete-popup',
    template: ''
})
export class SaleItemDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ saleItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SaleItemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.saleItem = saleItem;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sale-item', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sale-item', { outlets: { popup: null } }]);
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
