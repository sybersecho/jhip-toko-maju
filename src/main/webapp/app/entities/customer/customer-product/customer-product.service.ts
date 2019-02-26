import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { ICustomer, Gender, Customer } from 'app/shared/model/customer.model';

type EntityResponseType = HttpResponse<ICustomerProduct>;
type EntityArrayResponseType = HttpResponse<ICustomerProduct[]>;

@Injectable({ providedIn: 'root' })
export class CustomerProductService {
    public resourceUrl = SERVER_API_URL + 'api/customer-products';
    public resourceCustomerUrl = SERVER_API_URL + 'api/customer-products/customers';
    public resourceProductUrl = SERVER_API_URL + 'api/customer-products/products';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/customer-products';
    private products: ICustomerProduct[] = [];

    public customer: ICustomer;
    constructor(protected http: HttpClient) {
        this.customer = new Customer();
        this.customer.id = 1;
        this.customer.gender = Gender.FEMALE;
        this.customer.firstName = 'Fransisko';
        this.customer.lastName = 'Sanaky';
    }

    create(customerProduct: ICustomerProduct): Observable<EntityResponseType> {
        return this.http.post<ICustomerProduct>(this.resourceUrl, customerProduct, { observe: 'response' });
    }

    update(customerProduct: ICustomerProduct): Observable<EntityResponseType> {
        return this.http.put<ICustomerProduct>(this.resourceUrl, customerProduct, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        this.http
            .get<ICustomerProduct[]>(`${this.resourceCustomerUrl}/${id}`, { observe: 'response' })
            .subscribe((res: HttpResponse<ICustomerProduct[]>) => {
                this.products = res.body;
                console.log('**********');
                console.log(this.products);
            });
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

    findByCustomer(id: number): Observable<EntityArrayResponseType> {
        console.log('call by customer');
        return this.http.get<ICustomerProduct[]>(`${this.resourceCustomerUrl}/${id}`, { observe: 'response' });
    }

    saveOrUpdate(customerProducts: ICustomerProduct[]): Observable<EntityResponseType> {
        return this.http.put<ICustomerProduct>(this.resourceProductUrl, customerProducts, { observe: 'response' });
    }
}
