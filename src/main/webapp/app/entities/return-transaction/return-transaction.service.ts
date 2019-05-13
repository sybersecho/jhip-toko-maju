import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReturnTransaction } from 'app/shared/model/return-transaction.model';

type EntityResponseType = HttpResponse<IReturnTransaction>;
type EntityArrayResponseType = HttpResponse<IReturnTransaction[]>;

@Injectable({ providedIn: 'root' })
export class ReturnTransactionService {
    public resourceUrl = SERVER_API_URL + 'api/return-transactions';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/return-transactions';

    constructor(protected http: HttpClient) {}

    create(returnTransaction: IReturnTransaction): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(returnTransaction);
        return this.http
            .post<IReturnTransaction>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(returnTransaction: IReturnTransaction): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(returnTransaction);
        return this.http
            .put<IReturnTransaction>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IReturnTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReturnTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReturnTransaction[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(returnTransaction: IReturnTransaction): IReturnTransaction {
        const copy: IReturnTransaction = Object.assign({}, returnTransaction, {
            created_date:
                returnTransaction.created_date != null && returnTransaction.created_date.isValid()
                    ? returnTransaction.created_date.toJSON()
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.created_date = res.body.created_date != null ? moment(res.body.created_date) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((returnTransaction: IReturnTransaction) => {
                returnTransaction.created_date = returnTransaction.created_date != null ? moment(returnTransaction.created_date) : null;
            });
        }
        return res;
    }
}
