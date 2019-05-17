import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IBadStockProduct } from 'app/shared/model/bad-stock-product.model';
import { BadStockProductService } from './bad-stock-product.service';

@Component({
    selector: 'jhi-bad-stock-product-update',
    templateUrl: './bad-stock-product-update.component.html'
})
export class BadStockProductUpdateComponent implements OnInit {
    badStockProduct: IBadStockProduct;
    isSaving: boolean;

    constructor(protected badStockProductService: BadStockProductService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ badStockProduct }) => {
            this.badStockProduct = badStockProduct;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.badStockProduct.id !== undefined) {
            this.subscribeToSaveResponse(this.badStockProductService.update(this.badStockProduct));
        } else {
            this.subscribeToSaveResponse(this.badStockProductService.create(this.badStockProduct));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IBadStockProduct>>) {
        result.subscribe((res: HttpResponse<IBadStockProduct>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
