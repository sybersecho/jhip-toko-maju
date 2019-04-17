import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProject } from 'app/shared/model/project.model';

type EntityResponseType = HttpResponse<IProject>;
type EntityArrayResponseType = HttpResponse<IProject[]>;

@Injectable({ providedIn: 'root' })
export class ProjectService {
    public resourceUrl = SERVER_API_URL + 'api/projects';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/projects';

    constructor(protected http: HttpClient) {}

    create(project: IProject): Observable<EntityResponseType> {
        return this.http.post<IProject>(this.resourceUrl, project, { observe: 'response' });
    }

    update(project: IProject): Observable<EntityResponseType> {
        return this.http.put<IProject>(this.resourceUrl, project, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProject>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        const modifiyOption = this.modifiyOption(options);
        return this.http.get<IProject[]>(this.resourceUrl, { params: modifiyOption, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProject[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }

    protected modifiyOption(options: HttpParams): HttpParams {
        let modify: HttpParams = new HttpParams();
        if (options) {
            options.keys().forEach(key => {
                if (key === 'customerId') {
                    modify = modify.set('customerId.equals', options.get(key));
                } else {
                    modify = modify.set(key, options.get(key));
                }
            });
        }
        return modify;
    }
}
