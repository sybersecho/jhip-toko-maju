import { Component, OnInit } from '@angular/core';
import * as XLSX from 'xlsx';
import { ProductService } from '../product';
import { IProduct } from 'app/shared/model/product.model';
import { JhiAlertService } from 'ng-jhipster';
import { ExcelToJsonService } from 'app/shared/import/excel-to-json.service';
import { StockOrderProcess, IStockOrderProcess } from 'app/shared/model/stock-order-process.model';
import { StockOrderProcessService } from './stock-order-process.service';
import { IStockOrderReceive, StockOrderReceive } from 'app/shared/model/stock-order-receive.model';
import { ExcelModel } from 'app/shared/export/excel-model';
import { ExcelService } from 'app/shared/export/excel.service';

@Component({
    selector: 'jhi-stock-order-process',
    templateUrl: './stock-order-process.component.html',
    styles: []
})
export class StockOrderProcessComponent implements OnInit {
    arrayBuffer: any;
    file: File;
    arrayLoad: any[];
    barcodes: string[];
    products: IProduct[];
    stockOrderProcess: IStockOrderProcess[];
    stockOrderRecives: IStockOrderReceive[];

    constructor(
        protected productService: ProductService,
        protected stockOrderProcessService: StockOrderProcessService,
        protected jhiAlertService: JhiAlertService,
        protected importExcelService: ExcelToJsonService,
        protected excelService: ExcelService
    ) {}

    ngOnInit() {
        this.barcodes = [];
        this.products = [];
        this.stockOrderProcess = [];
        this.stockOrderRecives = [];
    }

    incomingfile(event) {
        this.file = event.target.files[0];
    }

    saveAndExport() {
        this.stockOrderProcessService.creates(this.stockOrderProcess).subscribe(
            res => {
                this.onSucessSave(res.body);
            },
            err => this.onError(err.message)
        );
    }

    onSucessSave(orderProcess: IStockOrderProcess[]) {
        this.createOrderReceive(orderProcess);
        this.extractOrderReceive();
        this.jhiAlertService.success('jhiptokomajuApp.stockOrder.process.created', null, null);
        this.stockOrderProcess = [];
        this.stockOrderRecives = [];
    }

    protected extractOrderReceive() {
        const excelModel = new ExcelModel();
        excelModel.data = this.stockOrderRecives;
        excelModel.fileName = 'Stock Orders Receive';
        excelModel.header = [];
        excelModel.title = '';

        this.excelService.generateExcel(excelModel);
    }

    protected createOrderReceive(orderProcess: IStockOrderProcess[]) {
        this.stockOrderRecives = [];
        orderProcess.forEach(it => {
            const orderReceive = new StockOrderReceive(it.barcode, it.name, it.quantityApprove);
            this.stockOrderRecives.push(orderReceive);
        });
    }

    importFile() {
        this.arrayLoad = [];
        this.barcodes = [];
        const fileReader = new FileReader();
        fileReader.onload = e => {
            this.arrayBuffer = fileReader.result;
            const data = new Uint8Array(this.arrayBuffer);
            const arr = new Array();
            for (let i = 0; i !== data.length; ++i) {
                arr[i] = String.fromCharCode(data[i]);
            }
            const bstr = arr.join('');
            const workbook = XLSX.read(bstr, { type: 'binary' });
            const first_sheet_name = workbook.SheetNames[0];
            const worksheet = workbook.Sheets[first_sheet_name];
            this.arrayLoad = XLSX.utils.sheet_to_json(worksheet, { raw: true });

            this.createBarcodes();
            this.getStockInHand();
        };
        fileReader.readAsArrayBuffer(this.file);
    }

    protected getStockInHand() {
        this.productService.findByBarcodes(this.barcodes).subscribe(res => this.onSuccess(res.body), error => this.onError(error.message));
    }

    onSuccess(products: IProduct[]): void {
        this.products = products;
        this.createOrder();
    }

    createOrder() {
        this.stockOrderProcess = [];
        this.arrayLoad.forEach(it => this.order(it));
    }

    order(it: any): void {
        const order = new StockOrderProcess();
        order.barcode = it.barcode;
        order.name = it.name;
        order.quantityApprove = it.quantity;
        order.stockInHand = this.getStock(it.barcode);
        order.quantityRequest = it.quantity;

        this.stockOrderProcess.push(order);
    }

    getStock(barcode: any): number {
        const index = this.products.findIndex(it => it.barcode === barcode);
        const stock = this.products[index].stock;
        return stock;
    }

    onError(message: any): void {
        console.error(message);
        this.jhiAlertService.error('error.somethingwrong');
    }

    protected createBarcodes() {
        this.arrayLoad.forEach(it => this.barcodes.push(it.barcode));
    }
}
