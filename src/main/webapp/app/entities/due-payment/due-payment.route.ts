import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DuePayment } from 'app/shared/model/due-payment.model';
import { DuePaymentService } from './due-payment.service';
import { DuePaymentComponent } from './due-payment.component';
import { DuePaymentDetailComponent } from './due-payment-detail.component';
import { DuePaymentUpdateComponent } from './due-payment-update.component';
import { DuePaymentDeletePopupComponent } from './due-payment-delete-dialog.component';
import { IDuePayment } from 'app/shared/model/due-payment.model';

@Injectable({ providedIn: 'root' })
export class DuePaymentResolve implements Resolve<IDuePayment> {
    constructor(private service: DuePaymentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDuePayment> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DuePayment>) => response.ok),
                map((duePayment: HttpResponse<DuePayment>) => duePayment.body)
            );
        }
        return of(new DuePayment());
    }
}

export const duePaymentRoute: Routes = [
    {
        path: 'due-payment',
        component: DuePaymentComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.duePayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'due-payment/:id/view',
        component: DuePaymentDetailComponent,
        resolve: {
            duePayment: DuePaymentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.duePayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'due-payment/new',
        component: DuePaymentUpdateComponent,
        resolve: {
            duePayment: DuePaymentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.duePayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'due-payment/:id/edit',
        component: DuePaymentUpdateComponent,
        resolve: {
            duePayment: DuePaymentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.duePayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const duePaymentPopupRoute: Routes = [
    {
        path: 'due-payment/:id/delete',
        component: DuePaymentDeletePopupComponent,
        resolve: {
            duePayment: DuePaymentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.duePayment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
