import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Customer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';
import { CustomerComponent } from './customer.component';
import { CustomerDetailComponent } from './customer-detail.component';
import { CustomerUpdateComponent } from './customer-update.component';
import { CustomerDeletePopupComponent } from './customer-delete-dialog.component';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerProductComponent } from './customer-product/customer-product.component';
import { CustomerProductResolve } from './customer-product/customer-product.route';
import { SearchProductComponent } from './search-product';

@Injectable({ providedIn: 'root' })
export class CustomerResolve implements Resolve<ICustomer> {
    constructor(private service: CustomerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICustomer> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            console.log('Resolve ' + id);
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Customer>) => response.ok),
                map((customer: HttpResponse<Customer>) => customer.body)
            );
        }
        console.log('Resolve ' + id);
        return of(new Customer());
    }
}

export const customerRoute: Routes = [
    {
        path: '',
        component: CustomerComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'jhiptokomajuApp.customer.home.search'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        // path: ':id/view',
        path: ':id/products',
        component: CustomerDetailComponent,
        children: [
            {
                path: '',
                component: CustomerProductComponent,
                resolve: {
                    customer: CustomerResolve,
                    customerProducts: CustomerProductResolve
                },
                canActivate: [UserRouteAccessService]
            }
        ],
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
        // path: ':id/view',
        path: ':id/add-products',
        component: CustomerDetailComponent,
        children: [
            {
                path: '',
                component: SearchProductComponent,
                resolve: {
                    pagingParams: JhiResolvePagingParams,
                    customer: CustomerResolve
                },
                data: {
                    authorities: ['ROLE_USER'],
                    defaultSort: 'id,asc',
                    pageTitle: 'jhiptokomajuApp.customer.home.title'
                },
                canActivate: [UserRouteAccessService]
            }
        ],
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
        path: 'new',
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
        path: ':id/edit',
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
        path: ':id/delete',
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
