import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IProductStock } from 'app/shared/model/product-stock.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { SearchProductStockDialogService } from './product-search-stock-dialog.component';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { IProduct } from 'app/shared/model/product.model';
import { ExcelModel } from 'app/shared/export/excel-model';
import { ExcelService } from 'app/shared/export/excel.service';

@Component({
    selector: 'jhi-product-stock',
    templateUrl: './product-stock.component.html'
})
export class ProductStockComponent implements OnInit, OnDestroy {
    productStocks: IProductStock[];
    // products: IProduct[];
    products: ProductStockModel[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;

    modalRef: NgbModalRef;
    eventSubscription: Subscription;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService,
        protected excelService: ExcelService,
        protected searchProductStockDialogService: SearchProductStockDialogService
    ) {
        this.productStocks = [];
        this.products = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    onSearchProduct() {
        this.modalRef = this.searchProductStockDialogService.open();
    }

    onDelete(i: number) {
        this.products.splice(i, 1);
    }

    exportToExcel(): void {
        const excelModel = new ExcelModel();
        excelModel.data = this.products;
        excelModel.fileName = 'Stock Product';
        excelModel.header = ['Barcode', 'Product Name', 'Unit', 'Project', 'Stock'];
        excelModel.title = 'Stock Product';
        excelModel.workSheetName = 'Stock Product';

        this.excelService.generateExcel(excelModel);
    }

    loadAll() {}

    reset() {
        this.page = 0;
        this.productStocks = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.productStocks = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.productStocks = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerEvents();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        this.modalRef = null;
    }

    trackId(index: number, item: IProductStock) {
        return item.id;
    }

    registerEvents() {
        this.eventSubscriber = this.eventManager.subscribe('addProductEvent', response => this.addProduct(response.content));
    }

    protected addProduct(selectedProduct: IProduct) {
        if (this.exist(selectedProduct.barcode)) {
            this.jhiAlertService.warning('warning.productExist', null, null);
            return;
        }
        const product = new ProductStockModel(
            selectedProduct.barcode,
            selectedProduct.name,
            selectedProduct.unitName,
            selectedProduct.stock
        );
        this.products.push(product);
    }

    protected exist(barcode: string): Boolean {
        return this.products.findIndex(p => p.barcode === barcode) > -1;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateProductStocks(data: IProductStock[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.productStocks.push(data[i]);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

export class ProductStockModel {
    constructor(public barcode?: string, public name?: string, public unitName?: string, public stock?: number) {}
}
