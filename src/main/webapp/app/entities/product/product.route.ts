import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Product } from 'app/shared/model/product.model';
import { ProductService } from './product.service';
import { ProductComponent } from './product.component';
import { ProductDetailComponent } from './product-detail.component';
import { ProductUpdateComponent } from './product-update.component';
import { ProductDeletePopupComponent } from './product-delete-dialog.component';
import { IProduct } from 'app/shared/model/product.model';
import { ExtractProductComponent } from './extract-product/extract-product.component';
import { ImportProductComponent } from './import-product/import-product.component';

@Injectable({ providedIn: 'root' })
export class ProductResolve implements Resolve<IProduct> {
    constructor(private service: ProductService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProduct> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Product>) => response.ok),
                map((product: HttpResponse<Product>) => product.body)
            );
        }
        return of(new Product());
    }
}

export const productRoute: Routes = [
    {
        path: 'product',
        // loadChildren:
        component: ProductComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_SUPERUSER'],
            defaultSort: 'id,asc',
            pageTitle: 'jhiptokomajuApp.product.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product/:id/view',
        component: ProductDetailComponent,
        resolve: {
            product: ProductResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.product.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product/new',
        component: ProductUpdateComponent,
        resolve: {
            product: ProductResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.product.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product/extract',
        component: ExtractProductComponent,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.product.extract.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product/import',
        component: ImportProductComponent,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.product.import.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product/:id/edit',
        component: ProductUpdateComponent,
        resolve: {
            product: ProductResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.product.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const productPopupRoute: Routes = [
    {
        path: 'product/:id/delete',
        component: ProductDeletePopupComponent,
        resolve: {
            product: ProductResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.product.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
