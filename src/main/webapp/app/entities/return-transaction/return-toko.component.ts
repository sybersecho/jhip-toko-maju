import { Component, OnInit, OnDestroy } from '@angular/core';
import { IReturnTransaction, ReturnTransaction, TransactionType } from 'app/shared/model/return-transaction.model';
import { CustomerService } from '../customer';
import { ICustomer } from 'app/shared/model/customer.model';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-return-toko',
    templateUrl: './return-toko.component.html',
    styles: []
})
export class ReturnTokoComponent implements OnInit, OnDestroy {
    returnToko: IReturnTransaction;
    currentAccount: any;
    eventSubcriptions: Subscription;

    constructor(
        protected customerService: CustomerService,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected jhiEventManager: JhiEventManager
    ) {
        this.returnToko = new ReturnTransaction();
        this.returnToko.transactionType = TransactionType.SHOP;
    }

    ngOnInit() {
        this.loadFirstCustomer();
        console.log('return: ', this.returnToko);
        this.accountService.identity().then(account => {
            this.currentAccount = account;
            this.setCreator();
        });
        this.registerEvent();
    }

    private setCreator() {
        this.returnToko.creatorId = this.currentAccount.id;
        this.returnToko.creatorLogin = this.currentAccount.login;
    }

    ngOnDestroy() {
        this.jhiEventManager.destroy(this.eventSubcriptions);
    }

    protected loadFirstCustomer() {
        this.customerService.findFirst().subscribe(
            res => {
                const customer: ICustomer = res.body;
                this.returnToko.customerId = customer.id;
                this.returnToko.customerCode = customer.code;
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
        this.eventSubcriptions = this.jhiEventManager.subscribe('onSaveReturnToko', res => {
            this.returnToko = res.content;
            this.returnToko.transactionType = TransactionType.SHOP;
            this.loadFirstCustomer();
            this.setCreator();
        });
    }
}
