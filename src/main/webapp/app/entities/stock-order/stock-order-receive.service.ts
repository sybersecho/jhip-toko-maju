import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { IStockOrderReceive } from 'app/shared/model/stock-order-receive.model';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<IStockOrderReceive>;
type EntityArrayResponseType = HttpResponse<IStockOrderReceive[]>;

@Injectable({
    providedIn: 'root'
})
export class StockOrderReceiveService {
    public resourceUrl = SERVER_API_URL + 'api/stock-order-receives';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/stock-order-receives';

    constructor(protected http: HttpClient) {}

    creates(receiveStockOrders: IStockOrderReceive[]): Observable<EntityResponseType> {
        return this.http
            .post<IStockOrderReceive>(this.resourceUrl + '/orders', receiveStockOrders, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
        }
        return res;
    }
}
