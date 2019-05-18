import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CancelTransaction } from 'app/shared/model/cancel-transaction.model';
import { CancelTransactionService } from './cancel-transaction.service';
import { CancelTransactionComponent } from './cancel-transaction.component';
import { CancelTransactionDetailComponent } from './cancel-transaction-detail.component';
import { CancelTransactionUpdateComponent } from './cancel-transaction-update.component';
import { CancelTransactionDeletePopupComponent } from './cancel-transaction-delete-dialog.component';
import { ICancelTransaction } from 'app/shared/model/cancel-transaction.model';
import { CancelTransactionCreateComponent } from './cancel-transaction-create.component';

@Injectable({ providedIn: 'root' })
export class CancelTransactionResolve implements Resolve<ICancelTransaction> {
    constructor(private service: CancelTransactionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICancelTransaction> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<CancelTransaction>) => response.ok),
                map((cancelTransaction: HttpResponse<CancelTransaction>) => cancelTransaction.body)
            );
        }
        return of(new CancelTransaction());
    }
}

export const cancelTransactionRoute: Routes = [
    {
        path: '',
        component: CancelTransactionComponent,
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.cancelTransaction.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    // {
    //     path: ':id/view',
    //     component: CancelTransactionDetailComponent,
    //     resolve: {
    //         cancelTransaction: CancelTransactionResolve
    //     },
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'jhiptokomajuApp.cancelTransaction.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
    {
        path: 'new',
        component: CancelTransactionCreateComponent,
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.cancelTransaction.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: CancelTransactionUpdateComponent,
        resolve: {
            cancelTransaction: CancelTransactionResolve
        },
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.cancelTransaction.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cancelTransactionPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CancelTransactionDeletePopupComponent,
        resolve: {
            cancelTransaction: CancelTransactionResolve
        },
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.cancelTransaction.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
