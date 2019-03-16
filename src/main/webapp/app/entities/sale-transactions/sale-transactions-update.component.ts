import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from './sale-transactions.service';

@Component({
    selector: 'jhi-sale-transactions-update',
    templateUrl: './sale-transactions-update.component.html'
})
export class SaleTransactionsUpdateComponent implements OnInit {
    saleTransactions: ISaleTransactions;
    isSaving: boolean;
    saleDate: string;

    constructor(protected saleTransactionsService: SaleTransactionsService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ saleTransactions }) => {
            this.saleTransactions = saleTransactions;
            this.saleDate = this.saleTransactions.saleDate != null ? this.saleTransactions.saleDate.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.saleTransactions.saleDate = this.saleDate != null ? moment(this.saleDate, DATE_TIME_FORMAT) : null;
        console.log('this.saleTransactions.saleDate: ' + this.saleTransactions.saleDate);
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
}
