import { Injectable } from '@angular/core';
import { IStockOrderProcess } from 'app/shared/model/stock-order-process.model';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

type EntityResponseType = HttpResponse<IStockOrderProcess>;
type EntityArrayResponseType = HttpResponse<IStockOrderProcess[]>;

@Injectable({
    providedIn: 'root'
})
export class StockOrderProcessService {
    public resourceUrl = SERVER_API_URL + 'api/stock-order-processes';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/stock-order-processes';

    constructor(protected http: HttpClient) {}

    creates(stockOrderProcess: IStockOrderProcess[]): Observable<EntityArrayResponseType> {
        // const copy = this.convertDateFromClient(stockOrderProcess);
        return this.http
            .post<IStockOrderProcess[]>(this.resourceUrl + '/orders', stockOrderProcess, { observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((stockOrder: IStockOrderProcess) => {
                stockOrder.createdDate = stockOrder.createdDate != null ? moment(stockOrder.createdDate) : null;
            });
        }
        return res;
    }

    protected convertDateFromClient(stockOrderProcess: IStockOrderProcess): IStockOrderProcess {
        const copy: IStockOrderProcess = Object.assign({}, stockOrderProcess, {
            createdDate:
                stockOrderProcess.createdDate != null && stockOrderProcess.createdDate.isValid()
                    ? stockOrderProcess.createdDate.toJSON()
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
        }
        return res;
    }
}
