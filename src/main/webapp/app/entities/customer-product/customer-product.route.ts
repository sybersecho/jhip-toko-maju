import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CustomerProduct } from 'app/shared/model/customer-product.model';
import { CustomerProductService } from './customer-product.service';
import { CustomerProductComponent } from './customer-product.component';
import { CustomerProductDetailComponent } from './customer-product-detail.component';
import { CustomerProductUpdateComponent } from './customer-product-update.component';
import { CustomerProductDeletePopupComponent } from './customer-product-delete-dialog.component';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';

@Injectable({ providedIn: 'root' })
export class CustomerProductResolve implements Resolve<ICustomerProduct[]> {
    private products: CustomerProduct[] = [];
    constructor(private service: CustomerProductService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICustomerProduct[]> {
        const id = route.params['id'] ? route.params['id'] : null;
        // if (id) {
        //     return this.service.find(id).pipe(
        //         filter((response: HttpResponse<CustomerProduct>) => response.ok),
        //         map((customerProduct: HttpResponse<CustomerProduct>) => customerProduct.body)
        //     );
        // }
        if (id) {
            return this.service.findByCustomer(id).pipe(
                filter((response: HttpResponse<CustomerProduct[]>) => response.ok),
                map((customerProducts: HttpResponse<CustomerProduct[]>) => customerProducts.body)
            );
        }
        return of(this.products);
    }
}

export const customerProductRoute: Routes = [
    // {
    //     path: '',
    //     component: CustomerProductComponent,
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'jhiptokomajuApp.customerProduct.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
    {
        path: ':id/view',
        component: CustomerProductComponent,
        resolve: {
            customerProducts: CustomerProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.customerProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CustomerProductUpdateComponent,
        // resolve: {
        //     customerProduct: CustomerProductResolve
        // },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.customerProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: CustomerProductUpdateComponent,
        resolve: {
            customerProduct: CustomerProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.customerProduct.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const customerProductPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: CustomerProductDeletePopupComponent,
        resolve: {
            customerProduct: CustomerProductResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.customerProduct.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
