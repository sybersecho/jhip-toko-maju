import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReturnItem } from 'app/shared/model/return-item.model';
import { ReturnItemService } from './return-item.service';

@Component({
    selector: 'jhi-return-item-delete-dialog',
    templateUrl: './return-item-delete-dialog.component.html'
})
export class ReturnItemDeleteDialogComponent {
    returnItem: IReturnItem;

    constructor(
        protected returnItemService: ReturnItemService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.returnItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'returnItemListModification',
                content: 'Deleted an returnItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-return-item-delete-popup',
    template: ''
})
export class ReturnItemDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ returnItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ReturnItemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.returnItem = returnItem;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/return-item', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/return-item', { outlets: { popup: null } }]);
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
