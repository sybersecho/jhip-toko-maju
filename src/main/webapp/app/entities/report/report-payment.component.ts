import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ReportService } from './report.service';
import * as moment from 'moment';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { IReportPayment } from 'app/shared/model/report-payment.model';
import { JhiAlertService } from 'ng-jhipster';

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

    protected onError(message: string) {
        console.error(message);
        this.jhiAlertService.error('error.somethingwrong');
    }
}
