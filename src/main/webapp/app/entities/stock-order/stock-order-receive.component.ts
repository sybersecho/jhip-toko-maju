import { Component, OnInit } from '@angular/core';
import * as XLSX from 'xlsx';
import { IStockOrderReceive } from 'app/shared/model/stock-order-receive.model';
import { StockOrderReceiveService } from './stock-order-receive.service';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-stock-order-receive',
    templateUrl: './stock-order-receive.component.html',
    styles: []
})
export class StockOrderReceiveComponent implements OnInit {
    arrayBuffer: any;
    file: File;
    arrayLoad: any[];
    receiveStockOrders: IStockOrderReceive[];

    constructor(protected stockOrderReceiveService: StockOrderReceiveService, protected jhiAlertService: JhiAlertService) {
        this.receiveStockOrders = [];
    }

    ngOnInit() {}

    incomingfile(event) {
        this.file = event.target.files[0];
    }

    importFile() {
        this.arrayLoad = [];
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
            console.log(this.arrayLoad);
            this.createReceiveOrder();
        };
        fileReader.readAsArrayBuffer(this.file);
    }

    createReceiveOrder() {
        this.receiveStockOrders = this.arrayLoad;
    }

    save() {
        this.stockOrderReceiveService
            .creates(this.receiveStockOrders)
            .subscribe(res => this.onSuccessSave(res.body), error => this.onError(error.message));
    }

    protected onSuccessSave(order: IStockOrderReceive): void {
        this.jhiAlertService.success('jhiptokomajuApp.stockOrder.receive.created', null, null);
        this.receiveStockOrders = [];
    }

    protected onError(message: any): void {
        console.error(message);
        this.jhiAlertService.error('error.somethingwrong');
    }
}
