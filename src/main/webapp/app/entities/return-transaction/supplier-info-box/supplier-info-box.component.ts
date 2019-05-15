import { Component, OnInit, Input, AfterViewInit, OnChanges, OnDestroy, SimpleChanges } from '@angular/core';
import { ISupplier } from 'app/shared/model/supplier.model';
import { IReturnTransaction } from 'app/shared/model/return-transaction.model';
import { Subscription } from 'rxjs';
import { SupplierService } from 'app/entities/supplier';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

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

    constructor(
        protected supplierService: SupplierService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.registerEvent();
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubscription);
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
            this.returnSupplier.customerCode = this.supplier.code;
            this.returnSupplier.customerId = this.supplier.id;
        });
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
