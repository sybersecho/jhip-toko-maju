import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ReturnTransaction } from 'app/shared/model/return-transaction.model';
import { ReturnTransactionService } from './return-transaction.service';
import { ReturnTransactionComponent } from './return-transaction.component';
import { ReturnTransactionDetailComponent } from './return-transaction-detail.component';
import { ReturnTransactionUpdateComponent } from './return-transaction-update.component';
import { ReturnTransactionDeletePopupComponent } from './return-transaction-delete-dialog.component';
import { IReturnTransaction } from 'app/shared/model/return-transaction.model';
import { ReturnTokoComponent } from './return-toko.component';
import { ReturnSupplierComponent } from './return-supplier.component';

@Injectable({ providedIn: 'root' })
export class ReturnTransactionResolve implements Resolve<IReturnTransaction> {
    constructor(private service: ReturnTransactionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IReturnTransaction> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ReturnTransaction>) => response.ok),
                map((returnTransaction: HttpResponse<ReturnTransaction>) => returnTransaction.body)
            );
        }
        return of(new ReturnTransaction());
    }
}

export const returnTransactionRoute: Routes = [
    {
        path: 'return-toko',
        component: ReturnTokoComponent,
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.returnTransaction.toko.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'return-supplier',
        component: ReturnSupplierComponent,
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.returnTransaction.supply.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ReturnTransactionDetailComponent,
        resolve: {
            returnTransaction: ReturnTransactionResolve
        },
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.returnTransaction.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ReturnTransactionUpdateComponent,
        resolve: {
            returnTransaction: ReturnTransactionResolve
        },
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.returnTransaction.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ReturnTransactionUpdateComponent,
        resolve: {
            returnTransaction: ReturnTransactionResolve
        },
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.returnTransaction.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const returnTransactionPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ReturnTransactionDeletePopupComponent,
        resolve: {
            returnTransaction: ReturnTransactionResolve
        },
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.returnTransaction.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
