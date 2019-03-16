import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from './sale-transactions.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
    selector: 'jhi-sale-transactions-update',
    templateUrl: './sale-transactions-update.component.html'
})
export class SaleTransactionsUpdateComponent implements OnInit {
    saleTransactions: ISaleTransactions;
    isSaving: boolean;

    customers: ICustomer[];
    saleDate: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected saleTransactionsService: SaleTransactionsService,
        protected customerService: CustomerService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ saleTransactions }) => {
            this.saleTransactions = saleTransactions;
            this.saleDate = this.saleTransactions.saleDate != null ? this.saleTransactions.saleDate.format(DATE_TIME_FORMAT) : null;
        });
        this.customerService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICustomer[]>) => response.body)
            )
            .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.saleTransactions.saleDate = this.saleDate != null ? moment(this.saleDate, DATE_TIME_FORMAT) : null;
        if (this.saleTransactions.id !== undefined) {
            this.subscribeToSaveResponse(this.saleTransactionsService.update(this.saleTransactions));
        } else {
            this.subscribeToSaveResponse(this.saleTransactionsService.create(this.saleTransactions));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISaleTransactions>>) {
        result.subscribe((res: HttpResponse<ISaleTransactions>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCustomerById(index: number, item: ICustomer) {
        return item.id;
    }
}
