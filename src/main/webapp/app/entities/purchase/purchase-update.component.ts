import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IPurchase } from 'app/shared/model/purchase.model';
import { PurchaseService } from './purchase.service';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService } from 'app/entities/supplier';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-purchase-update',
    templateUrl: './purchase-update.component.html'
})
export class PurchaseUpdateComponent implements OnInit {
    purchase: IPurchase;
    isSaving: boolean;

    suppliers: ISupplier[];

    users: IUser[];
    createdDate: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected purchaseService: PurchaseService,
        protected supplierService: SupplierService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ purchase }) => {
            this.purchase = purchase;
            this.createdDate = this.purchase.createdDate != null ? this.purchase.createdDate.format(DATE_TIME_FORMAT) : null;
        });
        this.supplierService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ISupplier[]>) => mayBeOk.ok),
                map((response: HttpResponse<ISupplier[]>) => response.body)
            )
            .subscribe((res: ISupplier[]) => (this.suppliers = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.purchase.createdDate = this.createdDate != null ? moment(this.createdDate, DATE_TIME_FORMAT) : null;
        if (this.purchase.id !== undefined) {
            this.subscribeToSaveResponse(this.purchaseService.update(this.purchase));
        } else {
            this.subscribeToSaveResponse(this.purchaseService.create(this.purchase));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchase>>) {
        result.subscribe((res: HttpResponse<IPurchase>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSupplierById(index: number, item: ISupplier) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
