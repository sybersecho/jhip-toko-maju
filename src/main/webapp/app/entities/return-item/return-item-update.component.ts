import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IReturnItem } from 'app/shared/model/return-item.model';
import { ReturnItemService } from './return-item.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { IReturnTransaction } from 'app/shared/model/return-transaction.model';
import { ReturnTransactionService } from 'app/entities/return-transaction';

@Component({
    selector: 'jhi-return-item-update',
    templateUrl: './return-item-update.component.html'
})
export class ReturnItemUpdateComponent implements OnInit {
    returnItem: IReturnItem;
    isSaving: boolean;

    products: IProduct[];

    returntransactions: IReturnTransaction[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected returnItemService: ReturnItemService,
        protected productService: ProductService,
        protected returnTransactionService: ReturnTransactionService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ returnItem }) => {
            this.returnItem = returnItem;
        });
        this.productService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IProduct[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProduct[]>) => response.body)
            )
            .subscribe((res: IProduct[]) => (this.products = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.returnTransactionService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IReturnTransaction[]>) => mayBeOk.ok),
                map((response: HttpResponse<IReturnTransaction[]>) => response.body)
            )
            .subscribe(
                (res: IReturnTransaction[]) => (this.returntransactions = res),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.returnItem.id !== undefined) {
            this.subscribeToSaveResponse(this.returnItemService.update(this.returnItem));
        } else {
            this.subscribeToSaveResponse(this.returnItemService.create(this.returnItem));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IReturnItem>>) {
        result.subscribe((res: HttpResponse<IReturnItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackProductById(index: number, item: IProduct) {
        return item.id;
    }

    trackReturnTransactionById(index: number, item: IReturnTransaction) {
        return item.id;
    }
}
