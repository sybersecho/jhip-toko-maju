import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from './supplier.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';

@Component({
    selector: 'jhi-supplier-update',
    templateUrl: './supplier-update.component.html'
})
export class SupplierUpdateComponent implements OnInit {
    supplier: ISupplier;
    isSaving: boolean;

    products: IProduct[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected supplierService: SupplierService,
        protected productService: ProductService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ supplier }) => {
            this.supplier = supplier;
        });
        this.productService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IProduct[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProduct[]>) => response.body)
            )
            .subscribe((res: IProduct[]) => (this.products = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.supplier.id !== undefined) {
            this.subscribeToSaveResponse(this.supplierService.update(this.supplier));
        } else {
            this.subscribeToSaveResponse(this.supplierService.create(this.supplier));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISupplier>>) {
        result.subscribe((res: HttpResponse<ISupplier>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
