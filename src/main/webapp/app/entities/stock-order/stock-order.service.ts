import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStockOrder } from 'app/shared/model/stock-order.model';

type EntityResponseType = HttpResponse<IStockOrder>;
type EntityArrayResponseType = HttpResponse<IStockOrder[]>;

@Injectable({ providedIn: 'root' })
export class StockOrderService {
    public resourceUrl = SERVER_API_URL + 'api/stock-orders';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/stock-orders';

    constructor(protected http: HttpClient) {}

    create(stockOrder: IStockOrder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(stockOrder);
        return this.http
            .post<IStockOrder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(stockOrder: IStockOrder): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(stockOrder);
        return this.http
            .put<IStockOrder>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IStockOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IStockOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IStockOrder[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(stockOrder: IStockOrder): IStockOrder {
        const copy: IStockOrder = Object.assign({}, stockOrder, {
            createdDate: stockOrder.createdDate != null && stockOrder.createdDate.isValid() ? stockOrder.createdDate.toJSON() : null,
            processedDate: stockOrder.processedDate != null && stockOrder.processedDate.isValid() ? stockOrder.processedDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
            res.body.processedDate = res.body.processedDate != null ? moment(res.body.processedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((stockOrder: IStockOrder) => {
                stockOrder.createdDate = stockOrder.createdDate != null ? moment(stockOrder.createdDate) : null;
                stockOrder.processedDate = stockOrder.processedDate != null ? moment(stockOrder.processedDate) : null;
            });
        }
        return res;
    }
}
