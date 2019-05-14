import { Component, OnInit, Input, AfterViewInit, OnChanges, SimpleChanges } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';
import { JhiAlertService } from 'ng-jhipster';
import { IReturnTransaction } from 'app/shared/model/return-transaction.model';

@Component({
    selector: 'jhi-customer-info-box',
    templateUrl: './customer-info-box.component.html',
    styles: []
})
export class CustomerInfoBoxComponent implements OnInit, AfterViewInit, OnChanges {
    customer?: ICustomer;
    // tslint:disable-next-line: no-input-rename
    @Input('customer') customerId: number;
    // tslint:disable-next-line: no-input-rename
    @Input('returnT') returnToko: IReturnTransaction;

    constructor(protected customerService: CustomerService, protected jhiAlertService: JhiAlertService) {}

    ngOnInit() {}

    getCustomerFullName(): string {
        if (!this.customer) {
            return '';
        }
        return this.customer.firstName + ' ' + this.customer.lastName;
    }

    ngAfterViewInit(): void {
        this.loadCustomer();
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.loadCustomer();
    }

    protected loadCustomer() {
        if (!this.returnToko.customerId) {
            return;
        }
        this.customerService.find(this.returnToko.customerId).subscribe(
            res => {
                this.customer = res.body;
            },
            err => {
                console.error(err.message);
                this.onError('error.somethingwrong');
            }
        );
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
