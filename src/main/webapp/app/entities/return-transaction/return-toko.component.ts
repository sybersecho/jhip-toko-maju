import { Component, OnInit } from '@angular/core';
import { IReturnTransaction, ReturnTransaction } from 'app/shared/model/return-transaction.model';
import { CustomerService } from '../customer';
import { ICustomer } from 'app/shared/model/customer.model';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-return-toko',
    templateUrl: './return-toko.component.html',
    styles: []
})
export class ReturnTokoComponent implements OnInit {
    returnToko: IReturnTransaction;
    constructor(protected customerService: CustomerService, protected jhiAlertService: JhiAlertService) {
        this.returnToko = new ReturnTransaction();
    }

    ngOnInit() {
        this.loadFirstCustomer();
        console.log('return: ', this.returnToko);
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
}
