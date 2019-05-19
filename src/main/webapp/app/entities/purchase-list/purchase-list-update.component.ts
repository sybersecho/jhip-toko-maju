import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPurchaseList } from 'app/shared/model/purchase-list.model';
import { PurchaseListService } from './purchase-list.service';
import { IPurchase } from 'app/shared/model/purchase.model';
import { PurchaseService } from 'app/entities/purchase';

@Component({
    selector: 'jhi-purchase-list-update',
    templateUrl: './purchase-list-update.component.html'
})
export class PurchaseListUpdateComponent implements OnInit {
    purchaseList: IPurchaseList;
    isSaving: boolean;

    purchases: IPurchase[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected purchaseListService: PurchaseListService,
        protected purchaseService: PurchaseService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ purchaseList }) => {
            this.purchaseList = purchaseList;
        });
        this.purchaseService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IPurchase[]>) => mayBeOk.ok),
                map((response: HttpResponse<IPurchase[]>) => response.body)
            )
            .subscribe((res: IPurchase[]) => (this.purchases = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.purchaseList.id !== undefined) {
            this.subscribeToSaveResponse(this.purchaseListService.update(this.purchaseList));
        } else {
            this.subscribeToSaveResponse(this.purchaseListService.create(this.purchaseList));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseList>>) {
        result.subscribe((res: HttpResponse<IPurchaseList>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackPurchaseById(index: number, item: IPurchase) {
        return item.id;
    }
}
