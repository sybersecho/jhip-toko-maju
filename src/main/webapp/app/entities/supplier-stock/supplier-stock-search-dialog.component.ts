import { Component, OnInit, Injectable } from '@angular/core';
import { ISupplier } from 'app/shared/model/supplier.model';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ActivatedRoute, Router } from '@angular/router';
import { SupplierService, SupplierSearchDialogComponent } from '../supplier';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-supplier-stock-search-dialog',
    templateUrl: './supplier-stock-search-dialog.component.html',
    styles: []
})
export class SupplierStockSearchDialogComponent implements OnInit {
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
    }
}

@Injectable({
    providedIn: 'root'
})
export class SupplierStockSearchDialogService {
    private isOpen = false;

    constructor(private modalService: NgbModal) {}

    open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }

        this.isOpen = true;
        const modalRef = this.modalService.open(SupplierStockSearchDialogComponent as Component, { size: 'lg', backdrop: 'static' });
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
