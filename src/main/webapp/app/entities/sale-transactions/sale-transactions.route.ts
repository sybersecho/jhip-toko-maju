import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleTransactionsService } from './sale-transactions.service';
import { SaleTransactionsComponent } from './sale-transactions.component';
import { SaleTransactionsDetailComponent } from './sale-transactions-detail.component';
import { SaleTransactionsUpdateComponent } from './sale-transactions-update.component';
import { SaleTransactionsDeletePopupComponent } from './sale-transactions-delete-dialog.component';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';

@Injectable({ providedIn: 'root' })
export class SaleTransactionsResolve implements Resolve<ISaleTransactions> {
    constructor(private service: SaleTransactionsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISaleTransactions> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SaleTransactions>) => response.ok),
                map((saleTransactions: HttpResponse<SaleTransactions>) => saleTransactions.body)
            );
        }
        return of(new SaleTransactions());
    }
}

export const saleTransactionsRoute: Routes = [
    {
        path: 'sale',
        component: SaleTransactionsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleTransactions.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale/:id/view',
        component: SaleTransactionsDetailComponent,
        resolve: {
            saleTransactions: SaleTransactionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleTransactions.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale/new',
        component: SaleTransactionsUpdateComponent,
        resolve: {
            saleTransactions: SaleTransactionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleTransactions.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale/:id/edit',
        component: SaleTransactionsUpdateComponent,
        resolve: {
            saleTransactions: SaleTransactionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleTransactions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const saleTransactionsPopupRoute: Routes = [
    {
        path: 'sale/:id/delete',
        component: SaleTransactionsDeletePopupComponent,
        resolve: {
            saleTransactions: SaleTransactionsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleTransactions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
