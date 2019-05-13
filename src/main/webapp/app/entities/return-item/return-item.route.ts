import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ReturnItem } from 'app/shared/model/return-item.model';
import { ReturnItemService } from './return-item.service';
import { ReturnItemComponent } from './return-item.component';
import { ReturnItemDetailComponent } from './return-item-detail.component';
import { ReturnItemUpdateComponent } from './return-item-update.component';
import { ReturnItemDeletePopupComponent } from './return-item-delete-dialog.component';
import { IReturnItem } from 'app/shared/model/return-item.model';

@Injectable({ providedIn: 'root' })
export class ReturnItemResolve implements Resolve<IReturnItem> {
    constructor(private service: ReturnItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IReturnItem> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ReturnItem>) => response.ok),
                map((returnItem: HttpResponse<ReturnItem>) => returnItem.body)
            );
        }
        return of(new ReturnItem());
    }
}

export const returnItemRoute: Routes = [
    {
        path: '',
        component: ReturnItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.returnItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ReturnItemDetailComponent,
        resolve: {
            returnItem: ReturnItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.returnItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ReturnItemUpdateComponent,
        resolve: {
            returnItem: ReturnItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.returnItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ReturnItemUpdateComponent,
        resolve: {
            returnItem: ReturnItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.returnItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const returnItemPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ReturnItemDeletePopupComponent,
        resolve: {
            returnItem: ReturnItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.returnItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
