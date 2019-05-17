import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBadStockProduct } from 'app/shared/model/bad-stock-product.model';

type EntityResponseType = HttpResponse<IBadStockProduct>;
type EntityArrayResponseType = HttpResponse<IBadStockProduct[]>;

@Injectable({ providedIn: 'root' })
export class BadStockProductService {
    public resourceUrl = SERVER_API_URL + 'api/bad-stock-products';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/bad-stock-products';

    constructor(protected http: HttpClient) {}

    create(badStockProduct: IBadStockProduct): Observable<EntityResponseType> {
        return this.http.post<IBadStockProduct>(this.resourceUrl, badStockProduct, { observe: 'response' });
    }

    update(badStockProduct: IBadStockProduct): Observable<EntityResponseType> {
        return this.http.put<IBadStockProduct>(this.resourceUrl, badStockProduct, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBadStockProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBadStockProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBadStockProduct[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
