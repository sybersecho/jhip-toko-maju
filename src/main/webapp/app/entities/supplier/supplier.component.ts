import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISupplier } from 'app/shared/model/supplier.model';
import { AccountService } from 'app/core';
import { SupplierService } from './supplier.service';

@Component({
    selector: 'jhi-supplier',
    templateUrl: './supplier.component.html'
})
export class SupplierComponent implements OnInit, OnDestroy {
    suppliers: ISupplier[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected supplierService: SupplierService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.supplierService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<ISupplier[]>) => res.ok),
                    map((res: HttpResponse<ISupplier[]>) => res.body)
                )
                .subscribe((res: ISupplier[]) => (this.suppliers = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.supplierService
            .query()
            .pipe(
                filter((res: HttpResponse<ISupplier[]>) => res.ok),
                map((res: HttpResponse<ISupplier[]>) => res.body)
            )
            .subscribe(
                (res: ISupplier[]) => {
                    this.suppliers = res;
                    this.currentSearch = '';
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSuppliers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISupplier) {
        return item.id;
    }

    registerChangeInSuppliers() {
        this.eventSubscriber = this.eventManager.subscribe('supplierListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
