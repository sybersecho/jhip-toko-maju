import { Component, OnInit, Input, AfterViewInit, OnChanges, OnDestroy, SimpleChanges } from '@angular/core';
import { ISupplier } from 'app/shared/model/supplier.model';
import { IReturnTransaction } from 'app/shared/model/return-transaction.model';
import { Subscription } from 'rxjs';
import { SupplierService, SearchSupplierDialogService } from 'app/entities/supplier';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-supplier-info-box',
    templateUrl: './supplier-info-box.component.html',
    styles: []
})
export class SupplierInfoBoxComponent implements OnInit, AfterViewInit, OnChanges, OnDestroy {
    supplier?: ISupplier;
    // tslint:disable-next-line: no-input-rename
    @Input('supplier') projectId: number;
    // tslint:disable-next-line: no-input-rename
    @Input('returnT') returnSupplier: IReturnTransaction;
    eventSubscription: Subscription;
    modalRef: NgbModalRef;

    constructor(
        protected supplierService: SupplierService,
        protected jhiAlertService: JhiAlertService,
        protected searchSupplierDialogService: SearchSupplierDialogService,
        protected eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.registerEvent();
    }

    searchSupplier() {
        console.log('search supplier');
        this.modalRef = this.searchSupplierDialogService.open();
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubscription);
        this.modalRef = null;
    }

    ngAfterViewInit(): void {
        this.loadSupplier();
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.loadSupplier();
    }

    protected loadSupplier() {
        if (!this.returnSupplier.supplierId) {
            return;
        }
        this.supplierService.find(this.returnSupplier.supplierId).subscribe(
            res => {
                this.supplier = res.body;
            },
            err => {
                console.error(err.message);
                this.onError('error.somethingwrong');
            }
        );
    }

    protected registerEvent() {
        this.eventSubscription = this.eventManager.subscribe('onSelectSupplierEvent', response => {
            this.supplier = response.data;
            this.returnSupplier.supplierCode = this.supplier.code;
            this.returnSupplier.supplierId = this.supplier.id;
        });
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
