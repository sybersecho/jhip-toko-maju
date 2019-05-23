import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ISaleReport } from 'app/shared/model/sale-report.model';
import { DatePipe } from '@angular/common';
import { ISaleReportDetail, SaleReportDetail } from 'app/shared/model/sale-report-detail.model';
import { SaleTransactionsService } from '../sale-transactions';
import { ExcelService } from 'app/shared/export/excel.service';
import moment = require('moment');
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleReportService } from './sale-report.service';
import * as FileSaver from 'file-saver';

const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

@Component({
    selector: 'jhi-sale-report',
    templateUrl: './sale-report.component.html'
})
export class SaleReportComponent implements OnInit, OnDestroy {
    fromDate: string;
    endDate: string;
    saleDetailReports: ISaleReportDetail[];

    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;

    constructor(
        protected datePipe: DatePipe,
        protected saleTransactionsService: SaleTransactionsService,
        protected saleReportService: SaleReportService,
        protected excelService: ExcelService,
        protected jhiAlertService: JhiAlertService
    ) {
        this.saleDetailReports = [];
    }

    search() {
        this.loadReport();
    }

    exportToExcel() {
        // const excelModel = new ExcelModel();
        // excelModel.data = this.saleDetailReports;
        // excelModel.fileName = 'Laporan Penjualan Detail';
        // excelModel.header = ['Barcode', 'Product Name', 'Unit', 'Project', 'Stock'];
        // excelModel.title = 'Stock Product';
        // excelModel.workSheetName = 'Stock Product';

        // this.excelService.generateExcel(excelModel);
        this.saleReportService
            .saleDetailReport({
                from: moment(this.fromDate)
                    .startOf('day')
                    .toJSON(),
                end: moment(this.endDate)
                    .endOf('day')
                    .toJSON()
            })
            .subscribe(
                res => {
                    this.saveAsExcelFile(res.body, 'Sale Report Details');
                },
                error => {
                    // console.error(error.message);
                    this.onError(error.message);
                }
            );
    }

    private saveAsExcelFile(buffer: any, fileName: string): void {
        const data: Blob = new Blob([buffer], {
            type: EXCEL_TYPE
        });
        FileSaver.saveAs(data, fileName + '_export_' + new Date().getTime() + EXCEL_EXTENSION);
    }

    ngOnInit() {
        this.currentMonth();
        this.loadReport();
    }

    protected currentMonth() {
        const dateFormat = 'yyyy-MM-dd';
        const today = new Date();
        const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
        const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
        this.fromDate = this.datePipe.transform(firstDay, dateFormat);
        this.endDate = this.datePipe.transform(lastDay, dateFormat);
    }

    loadReport() {
        this.saleDetailReports = [];
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

    protected onError(message: string) {
        console.error(message);
        this.jhiAlertService.error('error.somethingwrong', null, null);
    }

    protected paginateSaleTransaction(sales: ISaleTransactions[], headers: HttpHeaders): void {
        // this.resetTotals();
        // const copyOf = this.filterSale(sales);
        this.createReportModel(sales);
    }

    protected createReportModel(sales: ISaleTransactions[]) {
        sales.forEach(sale => this.addToList(sale));
    }

    protected addToList(sale: ISaleTransactions): void {
        const detailReport = new SaleReportDetail();
        detailReport.createFrom(sale);

        // this.totalDiscount += report.discount;
        // this.totalPaid += report.paid;
        // this.totalRemainingPayment += report.remainingPayment;
        // this.totalTransaction += report.totalPayment;

        this.saleDetailReports.push(detailReport);
    }

    ngOnDestroy() {
        // this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSaleReports() {
        // this.eventSubscriber = this.eventManager.subscribe('saleReportListModification', response => this.reset());
    }

    protected paginateSaleReports(data: ISaleReport[], headers: HttpHeaders) {
        // this.links = this.parseLinks.parse(headers.get('link'));
        // this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        // for (let i = 0; i < data.length; i++) {
        //     this.saleDetailReports.push(data[i]);
    }
}
