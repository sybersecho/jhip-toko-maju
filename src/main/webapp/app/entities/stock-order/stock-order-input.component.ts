import { Component, OnInit, OnDestroy } from '@angular/core';
import { StockOrder } from 'app/shared/model/stock-order.model';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SearchProductStockDialogService } from '../product-stock';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { IProduct } from 'app/shared/model/product.model';
import { ExcelModel } from 'app/shared/export/excel-model';
import { ExcelService } from 'app/shared/export/excel.service';

@Component({
    selector: 'jhi-stock-order-input',
    templateUrl: './stock-order-input.component.html',
    styles: []
})
export class StockOrderInputComponent implements OnInit, OnDestroy {
    stockOrders: StockOrder[];
    modalRef: NgbModalRef;
    eventSubscription: Subscription;
    totalOrder: number;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected searchProductStockDialogService: SearchProductStockDialogService,
        protected excelService: ExcelService
    ) {
        this.stockOrders = [];
        this.totalOrder = 0;
    }

    ngOnInit() {
        this.registerEvents();
        this.calculateTotalOrder();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscription);
        this.modalRef = null;
    }

    onSearchProduct() {
        this.modalRef = this.searchProductStockDialogService.open();
    }

    onChangeQuantity(i: number) {
        const changed: StockOrder = this.stockOrders[i];
        changed.totalPrice = changed.quantity * changed.unitPrice;
        this.stockOrders[i] = changed;
        if (changed.quantity <= 0) {
            this.onDelete(i);
        }
        this.calculateTotalOrder();
    }

    onDelete(i: number) {
        this.stockOrders.splice(i, 1);
    }

    calculateTotalOrder() {
        this.totalOrder = 0;
        this.stockOrders.forEach(it => {
            this.totalOrder += it.totalPrice;
        });
    }

    registerEvents() {
        this.eventSubscription = this.eventManager.subscribe('addProductEvent', response => this.addProduct(response.content));
    }

    protected addProduct(selectedProduct: IProduct) {
        if (this.exist(selectedProduct.barcode)) {
            this.jhiAlertService.warning('warning.productExist', null, null);
            return;
        }

        const order = new StockOrder(
            selectedProduct.barcode,
            selectedProduct.name,
            selectedProduct.unitName,
            selectedProduct.unitPrice,
            1,
            selectedProduct.unitPrice
        );

        this.stockOrders.push(order);
        this.calculateTotalOrder();
    }

    onExtract() {
        const excelModel = new ExcelModel();
        excelModel.data = this.stockOrders;
        excelModel.fileName = 'Stock Orders';
        excelModel.header = [];
        excelModel.title = '';

        this.excelService.generateExcel(excelModel);
        this.stockOrders = [];
    }

    protected exist(barcode: string): Boolean {
        return this.stockOrders.findIndex(p => p.barcode === barcode) > -1;
    }
}
