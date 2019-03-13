import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Customer } from 'app/shared/model/customer.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { SearchProductComponent } from '../product';

import {
    CustomerComponent,
    CustomerUpdateComponent,
    CustomerDeletePopupComponent,
    CustomerDetailComponent,
    CustomerService
} from '../customer';
import { CustomerProductComponent } from './customer-product/customer-product.component';
import { CustomerProductResolve } from './customer-product/customer-product-resolve.service';
import { InfoProductComponent } from './info-product/info-product.component';

@Injectable({ providedIn: 'root' })
export class CustomerResolve implements Resolve<ICustomer> {
    constructor(private service: CustomerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICustomer> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Customer>) => response.ok),
                map((customer: HttpResponse<Customer>) => customer.body)
            );
        }
        return of(new Customer());
    }
}

export const customerRoute: Routes = [
    {
        path: 'customer',
        component: CustomerComponent,
        children: [
            {
                path: '',
                component: InfoProductComponent,
                data: {
                    authorities: ['ROLE_USER'],
                    defaultSort: 'id,asc',
                    pageTitle: 'jhiptokomajuApp.customer.home.title'
                }
            },
            {
                path: ':id/products',
                component: CustomerProductComponent,
                resolve: {
                    customerProducts: CustomerProductResolve,
                    customer: CustomerResolve
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jhiptokomajuApp.customer.home.title'
                },
                canActivateChild: [UserRouteAccessService]
            },
            {
                path: ':id/search-product',
                component: SearchProductComponent,
                resolve: {
                    entity: CustomerResolve
                },
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jhiptokomajuApp.customer.home.title'
                },
                canActivate: [UserRouteAccessService]
            }
        ],
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'jhiptokomajuApp.customer.home.title'
        },
        canActivateChild: [UserRouteAccessService]
    },
    {
        path: 'customer:id/view',
        component: CustomerDetailComponent,
        resolve: {
            customer: CustomerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.customer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customer/new',
        component: CustomerUpdateComponent,
        resolve: {
            customer: CustomerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.customer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customer/:id/edit',
        component: CustomerUpdateComponent,
        resolve: {
            customer: CustomerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.customer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const customerPopupRoute: Routes = [
    {
        path: 'customer/:id/delete',
        component: CustomerDeletePopupComponent,
        resolve: {
            customer: CustomerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.customer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
