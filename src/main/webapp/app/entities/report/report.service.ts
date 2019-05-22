import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReport } from 'app/shared/model/report.model';
import { ReportPayment, IReportPayment } from 'app/shared/model/report-payment.model';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { IReportPaymentDetail } from 'app/shared/model/report-payment-detai.model';

type EntityResponseType = HttpResponse<IReport>;
type EntityArrayResponseType = HttpResponse<IReport[]>;
type ReportPaymentResponse = HttpResponse<IReportPayment>;
type ArrayReportPaymentResponse = HttpResponse<IReportPayment[]>;

@Injectable({ providedIn: 'root' })
export class ReportService {
    public resourceUrl = SERVER_API_URL + 'api/report';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/report';

    constructor(protected http: HttpClient) {}

    findReportPayment(req?: any): Observable<ArrayReportPaymentResponse> {
        const options = new HttpParams().set('saleDate.greaterOrEqualThan', req.from).set('saleDate.lessOrEqualThan', req.end);
        return this.http
            .get<IReportPayment[]>(this.resourceUrl + '/payment', { params: options, observe: 'response' })
            .pipe(map((res: ArrayReportPaymentResponse) => this.convertDateArrayFromServer(res)));
    }

    create(report: IReport): Observable<EntityResponseType> {
        return this.http.post<IReport>(this.resourceUrl, report, { observe: 'response' });
    }

    update(report: IReport): Observable<EntityResponseType> {
        return this.http.put<IReport>(this.resourceUrl, report, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReport[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }

    protected convertDateArrayFromServer(res: HttpResponse<IReportPayment[]>): ArrayReportPaymentResponse {
        if (res.body) {
            res.body.forEach((report: IReportPayment) => {
                report.saleDate = report.saleDate != null ? moment(report.saleDate) : null;
                report.paymentDetails.forEach((detail: IReportPaymentDetail) => {
                    detail.paymentDate = detail.paymentDate != null ? moment(detail.paymentDate) : null;
                });
            });
        }
        return res;
    }
}
