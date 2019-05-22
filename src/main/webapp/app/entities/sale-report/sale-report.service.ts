import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpParams, HttpResponse, HttpHeaders } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class SaleReportService {
    public resourceUrl = SERVER_API_URL + 'api';

    constructor(protected http: HttpClient) {}

    saleDetailReport(req?: any) {
        const options = new HttpParams().set('saleDate.greaterOrEqualThan', req.from).set('saleDate.lessOrEqualThan', req.end);

        const thisheader = new HttpHeaders({
            'Content-Type': 'application/vnd.ms-excel',
            Accept: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        });

        return this.http.get(this.resourceUrl + '/report/sale-report-detail', {
            params: options,
            observe: 'response',
            headers: thisheader,
            responseType: 'arraybuffer'
        });
    }
}
