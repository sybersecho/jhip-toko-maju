import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISaleItem } from 'app/shared/model/sale-item.model';

type EntityResponseType = HttpResponse<ISaleItem>;
type EntityArrayResponseType = HttpResponse<ISaleItem[]>;

@Injectable({ providedIn: 'root' })
export class SaleItemService {
    public resourceUrl = SERVER_API_URL + 'api/sale-items';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/sale-items';

    constructor(protected http: HttpClient) {}

    create(saleItem: ISaleItem): Observable<EntityResponseType> {
        return this.http.post<ISaleItem>(this.resourceUrl, saleItem, { observe: 'response' });
    }

    update(saleItem: ISaleItem): Observable<EntityResponseType> {
        return this.http.put<ISaleItem>(this.resourceUrl, saleItem, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISaleItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISaleItem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISaleItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
