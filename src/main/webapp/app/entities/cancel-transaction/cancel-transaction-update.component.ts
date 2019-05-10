import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ICancelTransaction } from 'app/shared/model/cancel-transaction.model';
import { CancelTransactionService } from './cancel-transaction.service';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from 'app/entities/sale-transactions';
import { ISaleItem } from 'app/shared/model/sale-item.model';

@Component({
    selector: 'jhi-cancel-transaction-update',
    templateUrl: './cancel-transaction-update.component.html'
})
export class CancelTransactionUpdateComponent implements OnInit, AfterViewInit {
    cancelTransaction: ICancelTransaction;
    isSaving: boolean;

    saletransactions: ISaleTransactions[];
    cancelDate: string;
    items: ISaleItem[];
    sale: ISaleTransactions;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected cancelTransactionService: CancelTransactionService,
        protected saleService: SaleTransactionsService,
        protected saleTransactionsService: SaleTransactionsService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ cancelTransaction }) => {
            this.cancelTransaction = cancelTransaction;
            this.cancelDate = this.cancelTransaction.cancelDate != null ? this.cancelTransaction.cancelDate.format(DATE_TIME_FORMAT) : null;
        });
        // this.saleTransactionsService
        //     .query({ 'cancelTransactionId.specified': 'false' })
        //     .pipe(
        //         filter((mayBeOk: HttpResponse<ISaleTransactions[]>) => mayBeOk.ok),
        //         map((response: HttpResponse<ISaleTransactions[]>) => response.body)
        //     )
        //     .subscribe(
        //         (res: ISaleTransactions[]) => {
        //             if (!this.cancelTransaction.saleTransactionsId) {
        //                 this.saletransactions = res;
        //             } else {
        //                 this.saleTransactionsService
        //                     .find(this.cancelTransaction.saleTransactionsId)
        //                     .pipe(
        //                         filter((subResMayBeOk: HttpResponse<ISaleTransactions>) => subResMayBeOk.ok),
        //                         map((subResponse: HttpResponse<ISaleTransactions>) => subResponse.body)
        //                     )
        //                     .subscribe(
        //                         (subRes: ISaleTransactions) => (this.saletransactions = [subRes].concat(res)),
        //                         (subRes: HttpErrorResponse) => this.onError(subRes.message)
        //                     );
        //             }
        //         },
        //         (res: HttpErrorResponse) => this.onError(res.message)
        //     );
    }

    ngAfterViewInit(): void {
        // throw new Error("Method not implemented.");
        this.saleService.find(this.cancelTransaction.saleTransactionsId).subscribe(
            res => {
                this.sale = res.body;
                this.sale.projectName = this.sale.projectId ? this.sale.projectName : '-';
                this.items = this.sale.items;
            },
            error => {
                this.onError('error.somethingwrong');
            }
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.cancelTransaction.cancelDate = this.cancelDate != null ? moment(this.cancelDate, DATE_TIME_FORMAT) : null;
        if (this.cancelTransaction.id !== undefined) {
            this.subscribeToSaveResponse(this.cancelTransactionService.update(this.cancelTransaction));
        } else {
            this.subscribeToSaveResponse(this.cancelTransactionService.create(this.cancelTransaction));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICancelTransaction>>) {
        result.subscribe((res: HttpResponse<ICancelTransaction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSaleTransactionsById(index: number, item: ISaleTransactions) {
        return item.id;
    }
}
