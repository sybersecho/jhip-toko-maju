import { Injectable } from '@angular/core';
import { IGeraiTransaction } from 'app/shared/model/gerai-transaction.model';
import { HttpResponse, HttpClient, HttpParams } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

type EntityResponseType = HttpResponse<IGeraiTransaction>;
type EntityArrayResponseType = HttpResponse<IGeraiTransaction[]>;

@Injectable({
    providedIn: 'root'
})
export class GeraiTransactionService {
    public resourceUrl = SERVER_API_URL + 'api/gerai-transactions';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/gerai-transactions';

    constructor(protected http: HttpClient) {}

    findByGeraiId(id: number): Observable<EntityArrayResponseType> {
        const options = new HttpParams().set('geraiId.equals', id.toString()).set('sort', 'id,desc');
        return this.http
            .get<IGeraiTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((geraiTransaction: IGeraiTransaction) => {
                geraiTransaction.receivedDate = geraiTransaction.receivedDate != null ? moment(geraiTransaction.receivedDate) : null;
            });
        }
        return res;
    }
}
