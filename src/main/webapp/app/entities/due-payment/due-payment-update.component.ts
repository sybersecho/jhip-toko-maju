import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IDuePayment } from 'app/shared/model/due-payment.model';
import { DuePaymentService } from './due-payment.service';
import { IUser, UserService } from 'app/core';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from 'app/entities/sale-transactions';

@Component({
    selector: 'jhi-due-payment-update',
    templateUrl: './due-payment-update.component.html'
})
export class DuePaymentUpdateComponent implements OnInit {
    duePayment: IDuePayment;
    isSaving: boolean;

    users: IUser[];

    saletransactions: ISaleTransactions[];
    createdDate: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected duePaymentService: DuePaymentService,
        protected userService: UserService,
        protected saleTransactionsService: SaleTransactionsService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ duePayment }) => {
            this.duePayment = duePayment;
            this.createdDate = this.duePayment.createdDate != null ? this.duePayment.createdDate.format(DATE_TIME_FORMAT) : null;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.saleTransactionsService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ISaleTransactions[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISaleTransactions[]>) => response.body)
            )
            .subscribe((res: ISaleTransactions[]) => (this.saletransactions = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.duePayment.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        if (this.duePayment.id !== undefined) {
            this.subscribeToSaveResponse(this.duePaymentService.update(this.duePayment));
        } else {
            this.subscribeToSaveResponse(this.duePaymentService.create(this.duePayment));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDuePayment>>) {
        result.subscribe((res: HttpResponse<IDuePayment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSaleTransactionsById(index: number, item: ISaleTransactions) {
        return item.id;
    }
}
