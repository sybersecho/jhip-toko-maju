import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

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
        return this.http.post<IStockOrder>(this.resourceUrl, stockOrder, { observe: 'response' });
    }

    update(stockOrder: IStockOrder): Observable<EntityResponseType> {
        return this.http.put<IStockOrder>(this.resourceUrl, stockOrder, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IStockOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStockOrder[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IStockOrder[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
