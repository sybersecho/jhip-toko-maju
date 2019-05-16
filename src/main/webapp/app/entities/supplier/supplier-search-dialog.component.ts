import { Component, OnInit, OnDestroy, Injectable } from '@angular/core';
import { NgbActiveModal, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ActivatedRoute, Router } from '@angular/router';
import { SupplierService } from './supplier.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-supplier-search-dialog',
    templateUrl: './supplier-search-dialog.component.html'
})
export class SupplierSearchDialogComponent implements OnInit {
    suppliers: ISupplier[];
    searchKeyword: string;

    constructor(
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected supplierService: SupplierService
    ) {}

    ngOnInit() {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    clearSearch() {
        this.searchKeyword = '';
        this.suppliers = null;
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return;
        }
        this.searchKeyword = query;
        this.loadAll();
    }

    loadAll(): void {
        if (this.searchKeyword) {
            this.supplierService.searchBy(this.searchKeyword).subscribe((res: HttpResponse<ISupplier[]>) => (this.suppliers = res.body));
            return;
        }
        this.suppliers = [];
    }

    onSelect(supplier: ISupplier) {
        this.eventManager.broadcast({
            name: 'onSelectSupplierEvent',
            data: supplier
        });
        this.activeModal.dismiss(true);
    }
}

@Injectable({
    providedIn: 'root'
})
export class SearchSupplierDialogService {
    private isOpen = false;

    constructor(private modalService: NgbModal) {}

    open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }

        this.isOpen = true;
        const modalRef = this.modalService.open(SupplierSearchDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        modalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return modalRef;
    }
}
