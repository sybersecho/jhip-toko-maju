import { Component, OnInit, OnDestroy } from '@angular/core';
import { IReturnTransaction, ReturnTransaction, TransactionType } from 'app/shared/model/return-transaction.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { Subscription } from 'rxjs';
import { SupplierService } from '../supplier';
import { ISupplier } from 'app/shared/model/supplier.model';

@Component({
    selector: 'jhi-return-supplier',
    templateUrl: './return-supplier.component.html',
    styles: []
})
export class ReturnSupplierComponent implements OnInit, OnDestroy {
    returnSupplier: IReturnTransaction;
    currentAccount: any;
    eventSubcriptions: Subscription;

    constructor(
        protected supplierService: SupplierService,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected jhiEventManager: JhiEventManager
    ) {
        this.returnSupplier = new ReturnTransaction();
        this.returnSupplier.transactionType = TransactionType.SUPPLIER;
    }

    ngOnInit() {
        this.loadFirstSupplier();
        console.log('return: ', this.returnSupplier);
        this.accountService.identity().then(account => {
            this.currentAccount = account;
            this.setCreator();
        });
        this.registerEvent();
    }

    private setCreator() {
        this.returnSupplier.creatorId = this.currentAccount.id;
        this.returnSupplier.creatorLogin = this.currentAccount.login;
    }

    ngOnDestroy() {
        this.jhiEventManager.destroy(this.eventSubcriptions);
    }

    protected loadFirstSupplier() {
        this.supplierService.findFirst().subscribe(
            res => {
                const supplier: ISupplier = res.body;
                this.returnSupplier.supplierId = supplier.id;
                this.returnSupplier.supplierCode = supplier.code;
            },
            error => {
                console.error(error.message);
                this.onError('error.somethingwrong');
            }
        );
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    protected registerEvent() {
        this.eventSubcriptions = this.jhiEventManager.subscribe('onSaveReturnTransaction', res => {
            this.returnSupplier = res.content;
            this.returnSupplier.transactionType = TransactionType.SUPPLIER;
            this.loadFirstSupplier();
            this.setCreator();
        });
    }
}
