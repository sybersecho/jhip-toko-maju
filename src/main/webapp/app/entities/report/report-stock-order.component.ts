import { Component, OnInit } from '@angular/core';
import { ExcelService } from 'app/shared/export/excel.service';
import { DatePipe } from '@angular/common';

@Component({
    selector: 'jhi-report-stock-order',
    templateUrl: './report-stock-order.component.html',
    styles: []
})
export class ReportStockOrderComponent implements OnInit {
    fromDate: string;
    endDate: string;

    constructor(protected datePipe: DatePipe, protected excelService: ExcelService) {}

    ngOnInit() {
        this.currentMonth();
        this.loadReport();
    }

    loadReport() {
        // throw new Error("Method not implemented.");
    }

    protected currentMonth() {
        const dateFormat = 'yyyy-MM-dd';
        // 'yyyy-MM-dd';
        const today = new Date();
        const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
        const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
        this.fromDate = this.datePipe.transform(firstDay, dateFormat);
        this.endDate = this.datePipe.transform(lastDay, dateFormat);
    }
}

export class ReportStockOrder {
    barcode?: string;
    name?: string;
    unitName?: string;
    unitPrice?: number;
    quantity?: number;
    totalPrice?: number;
}
