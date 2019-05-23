import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGerai } from 'app/shared/model/gerai.model';

type EntityResponseType = HttpResponse<IGerai>;
type EntityArrayResponseType = HttpResponse<IGerai[]>;

@Injectable({ providedIn: 'root' })
export class GeraiService {
    public resourceUrl = SERVER_API_URL + 'api/gerais';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/gerais';

    constructor(protected http: HttpClient) {}

    create(gerai: IGerai): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(gerai);
        return this.http
            .post<IGerai>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(gerai: IGerai): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(gerai);
        return this.http
            .put<IGerai>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IGerai>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IGerai[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IGerai[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(gerai: IGerai): IGerai {
        const copy: IGerai = Object.assign({}, gerai, {
            createdDate: gerai.createdDate != null && gerai.createdDate.isValid() ? gerai.createdDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((gerai: IGerai) => {
                gerai.createdDate = gerai.createdDate != null ? moment(gerai.createdDate) : null;
            });
        }
        return res;
    }
}
