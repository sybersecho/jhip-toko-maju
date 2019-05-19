import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IPurchase, Purchase } from 'app/shared/model/purchase.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { PurchaseService } from './purchase.service';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierService, SearchSupplierDialogService } from '../supplier';
import { SearchSupplierProductService } from './search-supplier-product/search-supplier-product.component';
import * as moment from 'moment';

@Component({
    selector: 'jhi-purchase',
    templateUrl: './purchase.component.html'
})
export class PurchaseComponent implements OnInit, OnDestroy {
    currentAccount: any;
    purchases: IPurchase[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    modalRef: NgbModalRef;
    supplier: ISupplier;
    purchase: IPurchase;

    constructor(
        protected purchaseService: PurchaseService,
        protected supplierService: SupplierService,
        protected searchSupplierDialogService: SearchSupplierDialogService,
        protected searchSupplierProductService: SearchSupplierProductService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected eventManager: JhiEventManager
    ) {
        this.purchase = new Purchase();
    }

    searchSupplier() {
        this.modalRef = this.searchSupplierDialogService.open();
    }

    searchProductOfSupplier() {
        if (!this.supplier) {
            console.log('no supplier selected');
            return;
        }
        this.searchSupplierProductService.open(this.supplier);
    }

    save() {
        this.purchase.creatorId = this.currentAccount.id;
        this.purchase.createdDate = moment();
        this.purchase.supplierId = this.supplier.id;
        this.purchaseService.create(this.purchase).subscribe(res => this.onSucccess(res.body), error => this.onError(error.message));
    }

    ngOnInit() {
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerEvents();
        this.loadFirstSupplier();
    }

    protected loadFirstSupplier() {
        this.supplierService.findFirst().subscribe(
            res => {
                this.supplier = res.body;
            },
            error => {
                console.error(error.message);
                this.onError('error.somethingwrong');
            }
        );
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        this.modalRef = null;
    }

    onDelete(i: number) {
        this.purchase.removeFromList(i);
    }

    onChangeQuantity(i: number) {
        this.purchase.changeQty(i);
    }

    changeUnitPrice() {
        this.purchase.calculateTotalPayment();
    }

    registerEvents() {
        this.eventSubscriber = this.eventManager.subscribe('onSelectSupplierEvent', response => {
            this.supplier = response.data;
            this.purchase.purchaseLists = [];
        });

        this.eventSubscriber = this.eventManager.subscribe('addProductEvent', response => {
            const result = this.purchase.addProduct(response.content);
            if (!result) {
                this.jhiAlertService.warning('warning.productExist', null, null);
            }
        });
    }

    protected onError(errorMessage: string) {
        console.error(errorMessage);
        this.jhiAlertService.error('error.somethingwrong', null, null);
    }

    protected onSucccess(body: IPurchase): void {
        console.log('success');
        this.loadFirstSupplier();
        this.purchase = new Purchase();
    }
}
