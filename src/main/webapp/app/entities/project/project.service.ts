import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

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
        const copy = this.convertDateFromClient(project);
        return this.http
            .post<IProject>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(project: IProject): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(project);
        return this.http
            .put<IProject>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IProject>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        const modifiyOption = this.modifiyOption(options);
        return this.http
            .get<IProject[]>(this.resourceUrl, { params: modifiyOption, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryCustomerProject(req?: any): Observable<EntityArrayResponseType> {
        const options = new HttpParams().set('customerId.equals', req.customerId);
        // createRequestOption(req);
        // const modifiyOption = this.modifiyOption(options);
        return this.http
            .get<IProject[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IProject[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(project: IProject): IProject {
        const copy: IProject = Object.assign({}, project, {
            createdDate: project.createdDate != null && project.createdDate.isValid() ? project.createdDate.toJSON() : null,
            modifiedDate: project.modifiedDate != null && project.modifiedDate.isValid() ? project.modifiedDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
            res.body.modifiedDate = res.body.modifiedDate != null ? moment(res.body.modifiedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((project: IProject) => {
                project.createdDate = project.createdDate != null ? moment(project.createdDate) : null;
                project.modifiedDate = project.modifiedDate != null ? moment(project.modifiedDate) : null;
            });
        }
        return res;
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
