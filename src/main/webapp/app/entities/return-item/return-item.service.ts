import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReturnItem } from 'app/shared/model/return-item.model';

type EntityResponseType = HttpResponse<IReturnItem>;
type EntityArrayResponseType = HttpResponse<IReturnItem[]>;

@Injectable({ providedIn: 'root' })
export class ReturnItemService {
    public resourceUrl = SERVER_API_URL + 'api/return-items';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/return-items';

    constructor(protected http: HttpClient) {}

    create(returnItem: IReturnItem): Observable<EntityResponseType> {
        return this.http.post<IReturnItem>(this.resourceUrl, returnItem, { observe: 'response' });
    }

    update(returnItem: IReturnItem): Observable<EntityResponseType> {
        return this.http.put<IReturnItem>(this.resourceUrl, returnItem, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IReturnItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReturnItem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReturnItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
