import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProduct } from 'app/shared/model/product.model';

type EntityResponseType = HttpResponse<IProduct>;
type EntityArrayResponseType = HttpResponse<IProduct[]>;

@Injectable({ providedIn: 'root' })
export class ProductService {
    public resourceUrl = SERVER_API_URL + 'api/products';
    public resourceSearchUrlBy = SERVER_API_URL + 'api/products-by';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/products';

    constructor(protected http: HttpClient) {}

    create(product: IProduct): Observable<EntityResponseType> {
        return this.http.post<IProduct>(this.resourceUrl, product, { observe: 'response' });
    }

    update(product: IProduct): Observable<EntityResponseType> {
        return this.http.put<IProduct>(this.resourceUrl, product, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProduct[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }

    searchBy(req?: any): Observable<EntityArrayResponseType> {
        console.log(req);
        const options = this.createParams(req);
        console.log('options');
        console.log(options);
        return this.http.get<IProduct[]>(this.resourceSearchUrlBy, { params: options, observe: 'response' });
    }

    protected createParams(req?: any): HttpParams {
        console.log(req);
        return new HttpParams()
            .set('barcode.contains', req.query)
            .set('name.contains', req.query)
            .set('supplierName.contains', req.query);
        // params = createRequestOption(params);
        // console.log('createRequestOption');
        // console.log(createRequestOption(params));
        // return params;
    }
}
