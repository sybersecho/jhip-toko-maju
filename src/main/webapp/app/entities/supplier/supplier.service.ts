import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISupplier } from 'app/shared/model/supplier.model';

type EntityResponseType = HttpResponse<ISupplier>;
type EntityArrayResponseType = HttpResponse<ISupplier[]>;

@Injectable({ providedIn: 'root' })
export class SupplierService {
    public resourceUrl = SERVER_API_URL + 'api/suppliers';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/suppliers';

    constructor(protected http: HttpClient) {}

    create(supplier: ISupplier): Observable<EntityResponseType> {
        return this.http.post<ISupplier>(this.resourceUrl, supplier, { observe: 'response' });
    }

    update(supplier: ISupplier): Observable<EntityResponseType> {
        return this.http.put<ISupplier>(this.resourceUrl, supplier, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISupplier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findFirst(): Observable<EntityResponseType> {
        return this.http.get<ISupplier>(this.resourceUrl + '/first', { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISupplier[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISupplier[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }

    searchBy(req: string) {
        const options = new HttpParams()
            .set('address.contains', req)
            .set('code.contains', req)
            .set('name.contains', req);
        return this.http.get<ISupplier[]>(this.resourceUrl, { params: options, observe: 'response' });
    }
}
