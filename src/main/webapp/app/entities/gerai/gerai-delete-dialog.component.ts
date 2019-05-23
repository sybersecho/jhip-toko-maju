import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGerai } from 'app/shared/model/gerai.model';
import { GeraiService } from './gerai.service';

@Component({
    selector: 'jhi-gerai-delete-dialog',
    templateUrl: './gerai-delete-dialog.component.html'
})
export class GeraiDeleteDialogComponent {
    gerai: IGerai;

    constructor(protected geraiService: GeraiService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.geraiService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'geraiListModification',
                content: 'Deleted an gerai'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-gerai-delete-popup',
    template: ''
})
export class GeraiDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gerai }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GeraiDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.gerai = gerai;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/gerai', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/gerai', { outlets: { popup: null } }]);
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
