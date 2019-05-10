import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICancelTransaction } from 'app/shared/model/cancel-transaction.model';

type EntityResponseType = HttpResponse<ICancelTransaction>;
type EntityArrayResponseType = HttpResponse<ICancelTransaction[]>;

@Injectable({ providedIn: 'root' })
export class CancelTransactionService {
    public resourceUrl = SERVER_API_URL + 'api/cancel-transactions';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/cancel-transactions';

    constructor(protected http: HttpClient) {}

    create(cancelTransaction: ICancelTransaction): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cancelTransaction);
        return this.http
            .post<ICancelTransaction>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(cancelTransaction: ICancelTransaction): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(cancelTransaction);
        return this.http
            .put<ICancelTransaction>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICancelTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICancelTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICancelTransaction[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(cancelTransaction: ICancelTransaction): ICancelTransaction {
        const copy: ICancelTransaction = Object.assign({}, cancelTransaction, {
            cancelDate:
                cancelTransaction.cancelDate != null && cancelTransaction.cancelDate.isValid()
                    ? cancelTransaction.cancelDate.toJSON()
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.cancelDate = res.body.cancelDate != null ? moment(res.body.cancelDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((cancelTransaction: ICancelTransaction) => {
                cancelTransaction.cancelDate = cancelTransaction.cancelDate != null ? moment(cancelTransaction.cancelDate) : null;
            });
        }
        return res;
    }
}
