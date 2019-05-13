import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IReturnTransaction } from 'app/shared/model/return-transaction.model';
import { ReturnTransactionService } from './return-transaction.service';
import { IUser, UserService } from 'app/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier';

@Component({
    selector: 'jhi-return-transaction-update',
    templateUrl: './return-transaction-update.component.html'
})
export class ReturnTransactionUpdateComponent implements OnInit {
    returnTransaction: IReturnTransaction;
    isSaving: boolean;

    users: IUser[];

    customers: ICustomer[];

    suppliers: ISupplier[];
    created_date: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected returnTransactionService: ReturnTransactionService,
        protected userService: UserService,
        protected customerService: CustomerService,
        protected supplierService: SupplierService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ returnTransaction }) => {
            this.returnTransaction = returnTransaction;
            this.created_date =
                this.returnTransaction.created_date != null ? this.returnTransaction.created_date.format(DATE_TIME_FORMAT) : null;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.customerService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICustomer[]>) => response.body)
            )
            .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.supplierService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ISupplier[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISupplier[]>) => response.body)
            )
            .subscribe((res: ISupplier[]) => (this.suppliers = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.returnTransaction.created_date = this.created_date != null ? moment(this.created_date, DATE_TIME_FORMAT) : null;
        if (this.returnTransaction.id !== undefined) {
            this.subscribeToSaveResponse(this.returnTransactionService.update(this.returnTransaction));
        } else {
            this.subscribeToSaveResponse(this.returnTransactionService.create(this.returnTransaction));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IReturnTransaction>>) {
        result.subscribe((res: HttpResponse<IReturnTransaction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackCustomerById(index: number, item: ICustomer) {
        return item.id;
    }

    trackSupplierById(index: number, item: ISupplier) {
        return item.id;
    }
}
