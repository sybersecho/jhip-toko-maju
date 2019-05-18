import { Component, OnInit, OnDestroy } from '@angular/core';
import { StockOrder, IStockOrder } from 'app/shared/model/stock-order.model';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SearchProductStockDialogService } from '../product-stock';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { IProduct } from 'app/shared/model/product.model';
import { ExcelModel } from 'app/shared/export/excel-model';
import { ExcelService } from 'app/shared/export/excel.service';
import { StockOrderService } from './stock-order.service';
import * as moment from 'moment';
import { AccountService } from 'app/core';
import { NgForm } from '@angular/forms';

@Component({
    selector: 'jhi-stock-order-input',
    templateUrl: './stock-order-input.component.html',
    styles: []
})
export class StockOrderInputComponent implements OnInit, OnDestroy {
    stockOrder: IStockOrder;
    modalRef: NgbModalRef;
    eventSubscription: Subscription;
    currentAccount: any;
    inputForm: NgForm;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected stockOrderService: StockOrderService,
        protected eventManager: JhiEventManager,
        protected searchProductStockDialogService: SearchProductStockDialogService,
        protected accountService: AccountService,
        protected excelService: ExcelService
    ) {
        this.stockOrder = new StockOrder();
    }

    ngOnInit() {
        this.registerEvents();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscription);
        this.modalRef = null;
    }

    onSearchProduct() {
        this.modalRef = this.searchProductStockDialogService.open();
    }

    onChangeQuantity(i: number) {
        this.stockOrder.changeQuantity(i);
    }

    onDelete(i: number) {
        this.stockOrder.removeRequest(i);
    }

    registerEvents() {
        this.eventSubscription = this.eventManager.subscribe('addProductEvent', response => this.addProduct(response.content));
    }

    protected addProduct(selectedProduct: IProduct) {
        const result = this.stockOrder.addStockRequest(selectedProduct);
        if (!result) {
            this.jhiAlertService.warning('warning.productExist', null, null);
        }
    }

    onExtract() {
        const excelModel = new ExcelModel();
        excelModel.data = this.stockOrder.stockOrderRequests;
        excelModel.fileName = 'Stock Orders';
        excelModel.header = [];
        excelModel.title = '';

        this.excelService.generateExcel(excelModel);
        this.stockOrder = new StockOrder();
        this.inputForm.resetForm();
    }

    saveAndExtract(form: NgForm) {
        this.inputForm = form;
        this.stockOrder.createdDate = moment();
        this.stockOrder.creatorId = this.currentAccount.id;
        this.stockOrder.creatorLogin = this.currentAccount.login;
        this.stockOrderService.create(this.stockOrder).subscribe(res => this.onSuccess(res.body), error => this.onError(error.message));
    }

    protected onSuccess(order: IStockOrder): void {
        this.onExtract();
    }

    protected onError(message: any): void {
        console.error(message);
        this.jhiAlertService.error('error.somethingwrong');
    }
}
