import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SaleItem } from 'app/shared/model/sale-item.model';
import { SaleItemService } from './sale-item.service';
import { SaleItemComponent } from './sale-item.component';
import { SaleItemDetailComponent } from './sale-item-detail.component';
import { SaleItemUpdateComponent } from './sale-item-update.component';
import { SaleItemDeletePopupComponent } from './sale-item-delete-dialog.component';
import { ISaleItem } from 'app/shared/model/sale-item.model';

@Injectable({ providedIn: 'root' })
export class SaleItemResolve implements Resolve<ISaleItem> {
    constructor(private service: SaleItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISaleItem> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SaleItem>) => response.ok),
                map((saleItem: HttpResponse<SaleItem>) => saleItem.body)
            );
        }
        return of(new SaleItem());
    }
}

export const saleItemRoute: Routes = [
    {
        path: '',
        component: SaleItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SaleItemDetailComponent,
        resolve: {
            saleItem: SaleItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SaleItemUpdateComponent,
        resolve: {
            saleItem: SaleItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SaleItemUpdateComponent,
        resolve: {
            saleItem: SaleItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const saleItemPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SaleItemDeletePopupComponent,
        resolve: {
            saleItem: SaleItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
