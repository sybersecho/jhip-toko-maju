import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomer } from 'app/shared/model/customer.model';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';

type EntityResponseType = HttpResponse<ICustomer>;
type EntityArrayResponseType = HttpResponse<ICustomer[]>;

@Injectable({ providedIn: 'root' })
export class CustomerService {
    public resourceUrl = SERVER_API_URL + 'api/customers';
    public resourceCustomerProductUrl = SERVER_API_URL + 'api/customer-products';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/customers';

    constructor(protected http: HttpClient) {}

    create(customer: ICustomer): Observable<EntityResponseType> {
        return this.http.post<ICustomer>(this.resourceUrl, customer, { observe: 'response' });
    }

    update(customer: ICustomer): Observable<EntityResponseType> {
        return this.http.put<ICustomer>(this.resourceUrl, customer, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICustomer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICustomer[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICustomer[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }

    searcyByCustomer(id: any): Observable<HttpResponse<ICustomerProduct[]>> {
        const options = new HttpParams().set('customerId.equals', id);
        return this.http.get<ICustomerProduct[]>(this.resourceCustomerProductUrl, { params: options, observe: 'response' });
    }

    searchByName(req?: any) {
        const options = new HttpParams().set('firstName.contains', req.query).set('lastName.contains', req.query);
        return this.http.get<ICustomerProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
    }
}
