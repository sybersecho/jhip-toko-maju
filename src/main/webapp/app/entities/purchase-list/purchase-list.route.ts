import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PurchaseList } from 'app/shared/model/purchase-list.model';
import { PurchaseListService } from './purchase-list.service';
import { PurchaseListComponent } from './purchase-list.component';
import { PurchaseListDetailComponent } from './purchase-list-detail.component';
import { PurchaseListUpdateComponent } from './purchase-list-update.component';
import { PurchaseListDeletePopupComponent } from './purchase-list-delete-dialog.component';
import { IPurchaseList } from 'app/shared/model/purchase-list.model';

@Injectable({ providedIn: 'root' })
export class PurchaseListResolve implements Resolve<IPurchaseList> {
    constructor(private service: PurchaseListService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPurchaseList> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<PurchaseList>) => response.ok),
                map((purchaseList: HttpResponse<PurchaseList>) => purchaseList.body)
            );
        }
        return of(new PurchaseList());
    }
}

export const purchaseListRoute: Routes = [
    {
        path: '',
        component: PurchaseListComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.purchaseList.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PurchaseListDetailComponent,
        resolve: {
            purchaseList: PurchaseListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.purchaseList.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PurchaseListUpdateComponent,
        resolve: {
            purchaseList: PurchaseListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.purchaseList.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: PurchaseListUpdateComponent,
        resolve: {
            purchaseList: PurchaseListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.purchaseList.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const purchaseListPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PurchaseListDeletePopupComponent,
        resolve: {
            purchaseList: PurchaseListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.purchaseList.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
