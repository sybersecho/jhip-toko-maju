import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGeraiConfig } from 'app/shared/model/gerai-config.model';

type EntityResponseType = HttpResponse<IGeraiConfig>;
type EntityArrayResponseType = HttpResponse<IGeraiConfig[]>;

@Injectable({ providedIn: 'root' })
export class GeraiConfigService {
    public resourceUrl = SERVER_API_URL + 'api/gerai-configs';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/gerai-configs';

    constructor(protected http: HttpClient) {}

    create(geraiConfig: IGeraiConfig): Observable<EntityResponseType> {
        return this.http.post<IGeraiConfig>(this.resourceUrl, geraiConfig, { observe: 'response' });
    }

    update(geraiConfig: IGeraiConfig): Observable<EntityResponseType> {
        return this.http.put<IGeraiConfig>(this.resourceUrl, geraiConfig, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGeraiConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGeraiConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGeraiConfig[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
