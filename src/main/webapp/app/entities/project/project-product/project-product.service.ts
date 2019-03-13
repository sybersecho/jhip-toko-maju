import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProjectProduct } from 'app/shared/model/project-product.model';

type EntityResponseType = HttpResponse<IProjectProduct>;
type EntityArrayResponseType = HttpResponse<IProjectProduct[]>;

@Injectable({ providedIn: 'root' })
export class ProjectProductService {
    public resourceUrl = SERVER_API_URL + 'api/project-products';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/project-products';

    constructor(protected http: HttpClient) {}

    create(projectProduct: IProjectProduct): Observable<EntityResponseType> {
        return this.http.post<IProjectProduct>(this.resourceUrl, projectProduct, { observe: 'response' });
    }

    update(projectProduct: IProjectProduct): Observable<EntityResponseType> {
        return this.http.put<IProjectProduct>(this.resourceUrl, projectProduct, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProjectProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findByProject(id: number): Observable<EntityArrayResponseType> {
        const options = new HttpParams().set('projectId.equals', id + '');
        return this.http.get<IProjectProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProjectProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProjectProduct[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
