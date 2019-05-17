import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProduct } from 'app/shared/model/product.model';
import { ExtractProductModel } from './extract-product/extract-product-model';
import { identifierModuleUrl } from '@angular/compiler';

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
        const options = this.createParams(req);
        return this.http.get<IProduct[]>(this.resourceSearchUrlBy, { params: options, observe: 'response' });
    }

    findByBarcode(barcode: string): Observable<EntityResponseType> {
        const options = new HttpParams().set('barcode.equals', barcode);
        return this.http.get<IProduct>(this.resourceSearchUrlBy, { params: options, observe: 'response' });
    }

    findBySupplierCode(supplierCode: string) {
        const options = new HttpParams().set('supplierCode.equals', supplierCode);
        return this.http.get<IProduct>(this.resourceSearchUrlBy, { params: options, observe: 'response' });
    }

    findBySupplierId(supplierId: number): Observable<EntityArrayResponseType> {
        const options = new HttpParams().set('supplierId.equals', supplierId + '');
        return this.http.get<IProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    extractProductById(id: number): Observable<HttpResponse<ExtractProductModel>> {
        const queryUrl = this.resourceUrl + '/extract-by-product';
        return this.http.get<ExtractProductModel>(`${queryUrl}/${id}`, { observe: 'response' });
    }

    extractProductBySupplier(supplierCode: string): Observable<HttpResponse<ExtractProductModel[]>> {
        const queryUrl = this.resourceUrl + '/extract-by-supplier';
        return this.http.get<ExtractProductModel[]>(`${queryUrl}/${supplierCode}`, { observe: 'response' });
    }

    protected createParams(req?: any): HttpParams {
        console.log(req);
        return new HttpParams()
            .set('barcode.contains', req.query)
            .set('name.contains', req.query)
            .set('supplierName.contains', req.query);
    }
}
