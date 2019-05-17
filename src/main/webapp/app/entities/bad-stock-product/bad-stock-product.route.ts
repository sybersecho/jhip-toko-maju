import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BadStockProduct } from 'app/shared/model/bad-stock-product.model';
import { BadStockProductService } from './bad-stock-product.service';
import { BadStockProductComponent } from './bad-stock-product.component';
import { BadStockProductDetailComponent } from './bad-stock-product-detail.component';
import { BadStockProductUpdateComponent } from './bad-stock-product-update.component';
import { BadStockProductDeletePopupComponent } from './bad-stock-product-delete-dialog.component';
import { IBadStockProduct } from 'app/shared/model/bad-stock-product.model';

@Injectable({ providedIn: 'root' })
export class BadStockProductResolve implements Resolve<IBadStockProduct> {
    constructor(private service: BadStockProductService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBadStockProduct> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<BadStockProduct>) => response.ok),
                map((badStockProduct: HttpResponse<BadStockProduct>) => badStockProduct.body)
            );
        }
        return of(new BadStockProduct());
    }
}

export const badStockProductRoute: Routes = [
    {
        path: '',
        component: BadStockProductComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'jhiptokomajuApp.badStockProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: BadStockProductDetailComponent,
        resolve: {
            badStockProduct: BadStockProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.badStockProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: BadStockProductUpdateComponent,
        resolve: {
            badStockProduct: BadStockProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.badStockProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: BadStockProductUpdateComponent,
        resolve: {
            badStockProduct: BadStockProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.badStockProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const badStockProductPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: BadStockProductDeletePopupComponent,
        resolve: {
            badStockProduct: BadStockProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.badStockProduct.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
