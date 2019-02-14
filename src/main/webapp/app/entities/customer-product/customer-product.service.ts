import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';

type EntityResponseType = HttpResponse<ICustomerProduct>;
type EntityArrayResponseType = HttpResponse<ICustomerProduct[]>;

@Injectable({ providedIn: 'root' })
export class CustomerProductService {
    public resourceUrl = SERVER_API_URL + 'api/customer-products';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/customer-products';

    constructor(protected http: HttpClient) {}

    create(customerProduct: ICustomerProduct): Observable<EntityResponseType> {
        return this.http.post<ICustomerProduct>(this.resourceUrl, customerProduct, { observe: 'response' });
    }

    update(customerProduct: ICustomerProduct): Observable<EntityResponseType> {
        return this.http.put<ICustomerProduct>(this.resourceUrl, customerProduct, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICustomerProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICustomerProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICustomerProduct[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
