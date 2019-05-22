import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ISaleReport, SaleReport } from 'app/shared/model/sale-report.model';
import { SaleTransactionsService } from '../sale-transactions';
import * as moment from 'moment';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { DATE_TIME_S_FORMAT } from 'app/shared';
import { ExcelModel } from 'app/shared/export/excel-model';
import { ExcelService } from 'app/shared/export/excel.service';

@Component({
    selector: 'jhi-sale-report-customer',
    templateUrl: './sale-report-customer.component.html',
    styles: []
})
export class SaleReportCustomerComponent implements OnInit {
    fromDate: string;
    endDate: string;
    saleReports: ISaleReport[];
    totalTransaction: number;
    totalDiscount: number;
    totalPaid: number;
    totalRemainingPayment: number;
    filter: any;

    constructor(
        protected datePipe: DatePipe,
        protected saleTransactionsService: SaleTransactionsService,
        protected excelService: ExcelService
    ) {
        this.saleReports = [];
        this.filter = 'all';
        this.resetTotals();
    }

    ngOnInit() {
        this.currentMonth();
        this.loadReport();
    }

    protected resetTotals() {
        this.totalDiscount = 0;
        this.totalPaid = 0;
        this.totalRemainingPayment = 0;
        this.totalTransaction = 0;
    }

    search() {
        this.saleReports = [];
        this.loadReport();
    }

    filterBy(key: any): void {
        this.filter = key;
        this.loadReport();
    }

    protected loadReport(): void {
        this.saleReports = [];
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
        const copyOf = this.filterSale(sales);
        this.createReportModel(copyOf);
    }

    filterSale(sales: ISaleTransactions[]): ISaleTransactions[] {
        const copyOf: ISaleTransactions[] = [];
        if (this.filter === 'all') {
            return sales;
        }

        for (let i = 0; i < sales.length; i++) {
            if (this.filter === 'nonProject' && !sales[i].projectId) {
                copyOf.push(sales[i]);
            } else if (this.filter === 'project' && sales[i].projectId) {
                copyOf.push(sales[i]);
            }
        }

        return copyOf;
    }

    protected createReportModel(sales: ISaleTransactions[]) {
        sales.forEach(sale => this.addToList(sale));
        console.log('reprot, ', this.saleReports);
    }

    protected addToList(sale: ISaleTransactions): void {
        const report = new SaleReport(
            sale.noInvoice,
            sale.customerFirstName + ' ' + sale.customerLastName,
            this.checkProjectName(sale.projectName),
            sale.totalPayment,
            sale.discount,
            sale.paid,
            sale.remainingPayment,
            sale.creatorLogin,
            sale.saleDate.format(DATE_TIME_S_FORMAT)
        );

        this.totalDiscount += report.discount;
        this.totalPaid += report.paid;
        this.totalRemainingPayment += report.remainingPayment;
        this.totalTransaction += report.totalPayment;

        this.saleReports.push(report);
    }

    protected checkProjectName(projectName: string): string {
        if (projectName) {
            return projectName;
        }
        return '-';
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
