import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInvoice } from 'app/shared/model/invoice.model';
import { formatDate } from '@angular/common';
import { ISaleItem } from 'app/shared/model/sale-item.model';

type EntityResponseType = HttpResponse<IInvoice>;
type EntityArrayResponseType = HttpResponse<IInvoice[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceService {
    public resourceUrl = SERVER_API_URL + 'api/invoices';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/invoices';

    constructor(protected http: HttpClient) {}

    create(invoice: IInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(invoice);
        return this.http
            .post<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(invoice: IInvoice): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(invoice);
        return this.http
            .put<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);

        return this.http
            .get<IInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryByDate(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        // options.set('saleDate.greaterThan', req.start);
        // options.set('saleDate.lessThan', req.end);
        const modOptions = this.modifiyOption(options);
        return this.http
            .get<IInvoice[]>(this.resourceUrl, { params: modOptions, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryByCustomer(req?: any) {
        const options = createRequestOption(req);
        const modOptions = this.modifiyOption(options);
        // const invoice = SERVER_API_URL + 'api/invoice-between-date';
        return this.http
            .get<IInvoice[]>(this.resourceUrl, { params: modOptions, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    queryInvoiceItems(req?: any) {
        const options = new HttpParams().set('noInvoice.equals', req.invoice);
        return this.http.get<ISaleItem[]>(this.resourceUrl + '/items', { params: options, observe: 'response' });
    }

    modifiyOption(options: HttpParams): HttpParams {
        let modify: HttpParams = new HttpParams();
        if (options) {
            options.keys().forEach(key => {
                if (key === 'start') {
                    modify = modify.set('saleDate.greaterOrEqualThan', options.get(key));
                } else if (key === 'end') {
                    modify = modify.set('saleDate.lessOrEqualThan', options.get(key));
                } else if (key === 'customer') {
                    modify = modify.set('customerId.equals', options.get(key));
                } else {
                    modify = modify.set(key, options.get(key));
                }
            });
            // if (req.sort) {
            //     req.sort.forEach(val => {
            //         options = options.append('sort', val);
            //     });
            // }
        }
        return modify;
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IInvoice[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(invoice: IInvoice): IInvoice {
        const copy: IInvoice = Object.assign({}, invoice, {
            saleDate: invoice.saleDate != null && invoice.saleDate.isValid() ? invoice.saleDate.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.saleDate = res.body.saleDate != null ? moment(res.body.saleDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((invoice: IInvoice) => {
                invoice.saleDate = invoice.saleDate != null ? moment(invoice.saleDate) : null;
            });
        }
        return res;
    }
}
