import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ISupplierStock } from 'app/shared/model/supplier-stock.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ISupplier } from 'app/shared/model/supplier.model';
import { SupplierStockSearchDialogService } from './supplier-stock-search-dialog.component';
import { ProductService } from '../product';
import { IProduct } from 'app/shared/model/product.model';
import { ExcelModel } from 'app/shared/export/excel-model';
import { ExcelService } from 'app/shared/export/excel.service';

@Component({
    selector: 'jhi-supplier-stock',
    templateUrl: './supplier-stock.component.html'
})
export class SupplierStockComponent implements OnInit, OnDestroy {
    supplierStocks: ISupplierStock[];
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
    products: ProductStockModel[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected activatedRoute: ActivatedRoute,
        protected supplierStockSearchDialogService: SupplierStockSearchDialogService,
        protected productService: ProductService,
        protected excelService: ExcelService,
        protected accountService: AccountService
    ) {
        this.supplierStocks = [];
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

    onSearchSupplierProduct() {
        this.modalRef = this.supplierStockSearchDialogService.open();
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
        this.supplierStocks = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.supplierStocks = [];
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
        this.supplierStocks = [];
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

    trackId(index: number, item: ISupplierStock) {
        return item.id;
    }

    registerEvents() {
        this.eventSubscriber = this.eventManager.subscribe('onSelectSupplierEvent', response =>
            this.searchProductBySupplier(response.data)
        );
    }

    searchProductBySupplier(supplier: ISupplier) {
        this.productService.findBySupplierId(supplier.id).subscribe(
            res => this.addProducts(res.body),
            error => {
                console.error(error.errorMessage);
            }
        );
        console.log(supplier);
    }

    protected addProducts(products: IProduct[]): void {
        this.createStockModel(products);
        console.log(this.products);
    }

    protected createStockModel(products: IProduct[]) {
        let isExist = false;
        products.forEach(it => {
            if (this.exist(it.barcode)) {
                isExist = true;
                return;
            }
            const p = new ProductStockModel(it.barcode, it.name, it.unitName, it.stock);
            this.products.push(p);
        });

        if (isExist) {
            this.jhiAlertService.warning('warning.productExist', null, null);
        }
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

    protected paginateSupplierStocks(data: ISupplierStock[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.supplierStocks.push(data[i]);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

export class ProductStockModel {
    constructor(public barcode?: string, public name?: string, public unitName?: string, public stock?: number) {}
}
