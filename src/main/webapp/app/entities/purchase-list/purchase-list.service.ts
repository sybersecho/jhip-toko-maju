import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPurchaseList } from 'app/shared/model/purchase-list.model';

type EntityResponseType = HttpResponse<IPurchaseList>;
type EntityArrayResponseType = HttpResponse<IPurchaseList[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseListService {
    public resourceUrl = SERVER_API_URL + 'api/purchase-lists';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/purchase-lists';

    constructor(protected http: HttpClient) {}

    create(purchaseList: IPurchaseList): Observable<EntityResponseType> {
        return this.http.post<IPurchaseList>(this.resourceUrl, purchaseList, { observe: 'response' });
    }

    update(purchaseList: IPurchaseList): Observable<EntityResponseType> {
        return this.http.put<IPurchaseList>(this.resourceUrl, purchaseList, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPurchaseList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPurchaseList[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPurchaseList[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
