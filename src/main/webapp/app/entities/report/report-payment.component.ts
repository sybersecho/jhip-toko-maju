import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ReportService } from './report.service';
import * as moment from 'moment';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { IReportPayment } from 'app/shared/model/report-payment.model';
import { JhiAlertService } from 'ng-jhipster';
import * as FileSaver from 'file-saver';

const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

@Component({
    selector: 'jhi-report-payment',
    templateUrl: './report-payment.component.html',
    styles: []
})
export class ReportPaymentComponent implements OnInit {
    fromDate: string;
    endDate: string;
    reportPayments: IReportPayment[];

    constructor(protected datePipe: DatePipe, protected reportService: ReportService, protected jhiAlertService: JhiAlertService) {
        this.reportPayments = [];
    }

    ngOnInit() {
        this.currentMonth();
        this.loadReport();
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

    search() {
        this.reportPayments = [];
        this.loadReport();
    }

    protected loadReport(): void {
        this.reportService
            .findReportPayment({
                from: moment(this.fromDate)
                    .startOf('day')
                    .toJSON(),
                end: moment(this.endDate)
                    .endOf('day')
                    .toJSON()
            })
            .subscribe(
                (res: HttpResponse<IReportPayment[]>) => this.paginateReportPayment(res.body, res.headers),
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                }
            );
    }

    protected paginateReportPayment(report: IReportPayment[], headers: HttpHeaders): void {
        this.reportPayments = report;
    }

    exportToExcel() {
        console.log('extract excel');

        this.reportService
            .extractReportPayment({
                from: moment(this.fromDate)
                    .startOf('day')
                    .toJSON(),
                end: moment(this.endDate)
                    .endOf('day')
                    .toJSON()
            })
            .subscribe(
                res => {
                    this.saveAsExcelFile(res.body, 'Report Payment');
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

    protected onError(message: string) {
        console.error(message);
        this.jhiAlertService.error('error.somethingwrong');
    }
}
