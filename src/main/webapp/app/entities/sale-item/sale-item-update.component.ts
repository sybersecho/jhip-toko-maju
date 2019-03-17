import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISaleItem } from 'app/shared/model/sale-item.model';
import { SaleItemService } from './sale-item.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from 'app/entities/sale-transactions';

@Component({
    selector: 'jhi-sale-item-update',
    templateUrl: './sale-item-update.component.html'
})
export class SaleItemUpdateComponent implements OnInit {
    saleItem: ISaleItem;
    isSaving: boolean;

    products: IProduct[];

    saletransactions: ISaleTransactions[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected saleItemService: SaleItemService,
        protected productService: ProductService,
        protected saleTransactionsService: SaleTransactionsService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ saleItem }) => {
            this.saleItem = saleItem;
        });
        this.productService
            .query({ 'saleItemId.specified': 'false' })
            .pipe(
                filter((mayBeOk: HttpResponse<IProduct[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProduct[]>) => response.body)
            )
            .subscribe(
                (res: IProduct[]) => {
                    if (!this.saleItem.productId) {
                        this.products = res;
                    } else {
                        this.productService
                            .find(this.saleItem.productId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IProduct>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IProduct>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IProduct) => (this.products = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
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
        if (this.saleItem.id !== undefined) {
            this.subscribeToSaveResponse(this.saleItemService.update(this.saleItem));
        } else {
            this.subscribeToSaveResponse(this.saleItemService.create(this.saleItem));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISaleItem>>) {
        result.subscribe((res: HttpResponse<ISaleItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSaleTransactionsById(index: number, item: ISaleTransactions) {
        return item.id;
    }
}
