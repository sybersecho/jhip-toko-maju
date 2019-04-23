import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDuePayment } from 'app/shared/model/due-payment.model';

type EntityResponseType = HttpResponse<IDuePayment>;
type EntityArrayResponseType = HttpResponse<IDuePayment[]>;

@Injectable({ providedIn: 'root' })
export class DuePaymentService {
    public resourceUrl = SERVER_API_URL + 'api/due-payments';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/due-payments';

    constructor(protected http: HttpClient) {}

    create(duePayment: IDuePayment): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(duePayment);
        return this.http
            .post<IDuePayment>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(duePayment: IDuePayment): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(duePayment);
        return this.http
            .put<IDuePayment>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDuePayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDuePayment[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDuePayment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(duePayment: IDuePayment): IDuePayment {
        const copy: IDuePayment = Object.assign({}, duePayment, {
            createdDate: duePayment.createdDate != null && duePayment.createdDate.isValid() ? duePayment.createdDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((duePayment: IDuePayment) => {
                duePayment.createdDate = duePayment.createdDate != null ? moment(duePayment.createdDate) : null;
            });
        }
        return res;
    }
}
