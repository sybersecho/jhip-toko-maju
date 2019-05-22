import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Report } from 'app/shared/model/report.model';
import { ReportService } from './report.service';
import { ReportComponent } from './report.component';
import { IReport } from 'app/shared/model/report.model';
import { ReportStockOrderComponent } from './report-stock-order.component';
import { ReportPaymentComponent } from './report-payment.component';

@Injectable({ providedIn: 'root' })
export class ReportResolve implements Resolve<IReport> {
    constructor(private service: ReportService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IReport> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Report>) => response.ok),
                map((report: HttpResponse<Report>) => report.body)
            );
        }
        return of(new Report());
    }
}

export const reportRoute: Routes = [
    {
        path: '',
        component: ReportComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.report.payment.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'payment',
        component: ReportPaymentComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.report.payment.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'stock-order',
        component: ReportStockOrderComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.report.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
