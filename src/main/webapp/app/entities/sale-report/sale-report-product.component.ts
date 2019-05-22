import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { SaleTransactionsService } from '../sale-transactions';
import { ExcelService } from 'app/shared/export/excel.service';
import * as moment from 'moment';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { DATE_TIME_S_FORMAT } from 'app/shared';
import { ExcelModel } from 'app/shared/export/excel-model';
import { ISaleReportProduct, SaleReportProduct } from 'app/shared/model/sale-report-product.model';

@Component({
    selector: 'jhi-sale-report-product',
    templateUrl: './sale-report-product.component.html',
    styles: []
})
export class SaleReportProductComponent implements OnInit {
    fromDate: string;
    endDate: string;
    saleReports: ISaleReportProduct[];
    totalTransaction: number;

    constructor(
        protected datePipe: DatePipe,
        protected saleTransactionsService: SaleTransactionsService,
        protected excelService: ExcelService
    ) {
        this.saleReports = [];
        this.resetTotals();
    }

    ngOnInit() {
        this.currentMonth();
        this.loadReport();
    }

    protected resetTotals() {
        this.totalTransaction = 0;
    }

    search() {
        this.saleReports = [];
        this.loadReport();
    }

    protected loadReport(): void {
        this.saleTransactionsService
            .queryByDate({
                from: moment(this.fromDate)
                    .startOf('day')
                    .toJSON(),
                end: moment(this.endDate)
                    .endOf('day')
                    .toJSON()
            })
            .subscribe(
                (res: HttpResponse<ISaleTransactions[]>) => this.paginateSaleTransaction(res.body, res.headers),
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            );
    }

    protected paginateSaleTransaction(sales: ISaleTransactions[], headers: HttpHeaders): void {
        this.resetTotals();
        this.createReportModel(sales);
    }

    protected createReportModel(sales: ISaleTransactions[]) {
        sales.forEach(sale => this.addToList(sale));
        console.log('reprot, ', this.saleReports);
    }

    protected addToList(sale: ISaleTransactions): void {
        sale.items.forEach(item => {
            const report = new SaleReportProduct(
                item.barcode,
                item.productName,
                item.unit,
                item.sellingPrice,
                item.quantity,
                item.totalPrice,
                sale.saleDate.format(DATE_TIME_S_FORMAT)
            );
            this.totalTransaction += report.totalPrice;
            this.saleReports.push(report);
        });
    }

    exportToExcel(): void {
        const excelModel = new ExcelModel();
        excelModel.data = this.saleReports;
        excelModel.fileName = 'Laporan Penjualan Customer';
        excelModel.header = ['Barcode', 'Product Name', 'Unit', 'Project', 'Stock'];
        excelModel.title = 'Stock Product';
        excelModel.workSheetName = 'Stock Product';

        this.excelService.generateExcel(excelModel);
    }

    protected onError(message: string) {
        console.log(message);
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
