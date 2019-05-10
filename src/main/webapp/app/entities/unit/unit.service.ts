import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUnit } from 'app/shared/model/unit.model';

type EntityResponseType = HttpResponse<IUnit>;
type EntityArrayResponseType = HttpResponse<IUnit[]>;

@Injectable({ providedIn: 'root' })
export class UnitService {
    public resourceUrl = SERVER_API_URL + 'api/units';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/units';

    constructor(protected http: HttpClient) {}

    create(unit: IUnit): Observable<EntityResponseType> {
        return this.http.post<IUnit>(this.resourceUrl, unit, { observe: 'response' });
    }

    update(unit: IUnit): Observable<EntityResponseType> {
        return this.http.put<IUnit>(this.resourceUrl, unit, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUnit[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
