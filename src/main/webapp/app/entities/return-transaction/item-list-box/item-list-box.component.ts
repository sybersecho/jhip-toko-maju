import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { IReturnTransaction, ReturnTransaction } from 'app/shared/model/return-transaction.model';
import * as moment from 'moment';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { ReturnTransactionService } from '..';

@Component({
    selector: 'jhi-item-list-box',
    templateUrl: './item-list-box.component.html',
    styles: []
})
export class ItemListBoxComponent implements OnInit, OnChanges {
    // tslint:disable-next-line: no-input-rename
    @Input('returnT') returnToko: IReturnTransaction;
    cashReturn = false;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected returnTransactionService: ReturnTransactionService,
        protected jhiEventManager: JhiEventManager
    ) {}

    ngOnInit() {}

    save() {
        this.returnToko.created_date = moment();
        this.subscribeToSaveResponse(this.returnTransactionService.create(this.returnToko));
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IReturnTransaction>>) {
        result.subscribe(
            (res: HttpResponse<IReturnTransaction>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => {
                console.error(res.message);
                this.onSaveError('error.somethingwrong');
            }
        );
    }

    protected onSaveSuccess(returnToko: IReturnTransaction) {
        // print
        this.returnToko = new ReturnTransaction();
        this.returnToko.calculateTotalReturn();
        this.jhiEventManager.broadcast({ name: 'onSaveReturnTransaction', content: this.returnToko });
        // this.jhiAlertService.success('jhiptokomajuApp.returnTransaction.saved', null, null);
    }

    protected onSaveError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.returnToko.calculateTotalReturn();
    }

    onDeleteItem(i: number) {
        this.returnToko.removeItemAt(i);
    }

    onChangeQuantity(i: number, itemQuantity: number) {
        if (itemQuantity <= 0) {
            this.onDeleteItem(i);
        } else {
            this.returnToko.updateQuantity(i, itemQuantity);
        }
        // this.addSaleIntoSession();
    }
}
