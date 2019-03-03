import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

import { SERVER_API_URL } from 'app/app.constants';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { createRequestOption } from 'app/shared';

type EntityResponseType = HttpResponse<ICustomerProduct>;
type EntityArrayResponseType = HttpResponse<ICustomerProduct[]>;

@Injectable({
    providedIn: 'root'
})
export class CustomerProductService {
    public resourceUrl = SERVER_API_URL + '/api/customer-products';
    public resourceCustomerUrl = SERVER_API_URL + 'api/customer-products/customers';
    public resourceProductUrl = SERVER_API_URL + 'api/customer-products/products';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/customer-products';
    private products: ICustomerProduct[] = [];

    constructor(protected http: HttpClient) {}

    create(customerProduct: ICustomerProduct): Observable<EntityResponseType> {
        return this.http.post<ICustomerProduct>(this.resourceUrl, customerProduct, { observe: 'response' });
    }

    update(customerProduct: ICustomerProduct): Observable<EntityResponseType> {
        return this.http.put<ICustomerProduct>(this.resourceUrl, customerProduct, { observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICustomerProduct[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }

    batchUpdate(customerProducts: ICustomerProduct[]) {
        return this.http.put<ICustomerProduct[]>(this.resourceUrl + '/products', customerProducts, { observe: 'response' });
    }

    // saveOrUpdate(customerProducts: ICustomerProduct[]): Observable<EntityResponseType> {
    //     return this.http.put<ICustomerProduct>(this.resourceProductUrl, customerProducts, { observe: 'response' });
    // }
}
