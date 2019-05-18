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
import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from '../invoice';
import { IProject } from 'app/shared/model/project.model';
// import { ProjectService } from 'app/entities/project';
import { ProjectService } from '../project';

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

@Injectable({ providedIn: 'root' })
export class CustomerInvoiceResolve implements Resolve<IInvoice[]> {
    private invoices: IInvoice[];
    constructor(private service: InvoiceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): IInvoice[] | Observable<IInvoice[]> | Promise<IInvoice[]> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .queryByCustomer({
                    customer: id
                })
                .pipe(
                    filter((response: HttpResponse<IInvoice[]>) => response.ok),
                    map((invoices: HttpResponse<IInvoice[]>) => invoices.body)
                );
        }
        return this.invoices;
    }
}

@Injectable({ providedIn: 'root' })
export class CustomerProjectResolve implements Resolve<IProject[]> {
    projects: IProject[];

    constructor(protected service: ProjectService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): IProject[] | Observable<IProject[]> | Promise<IProject[]> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service
                .query({
                    customerId: id
                })
                .pipe(
                    filter((response: HttpResponse<IProject[]>) => response.ok),
                    map((projects: HttpResponse<IProject[]>) => projects.body)
                );
        }
        return this.projects;
    }
}

export const customerRoute: Routes = [
    {
        path: 'customer',
        component: CustomerComponent,
        // children: [
        //     {
        //         path: '',
        //         component: InfoProductComponent,
        //         data: {
        //             authorities: ['ROLE_USER'],
        //             defaultSort: 'id,asc',
        //             pageTitle: 'jhiptokomajuApp.customer.home.title'
        //         }
        //     }
        // ],
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            defaultSort: 'id,asc',
            pageTitle: 'jhiptokomajuApp.customer.home.title'
        },
        canActivateChild: [UserRouteAccessService]
    },
    {
        path: 'customer/:id/products',
        component: CustomerProductComponent,
        resolve: {
            customerProducts: CustomerProductResolve,
            customer: CustomerResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.customer.home.title'
        },
        canActivateChild: [UserRouteAccessService]
    },
    {
        path: 'customer/:id/search-product',
        component: SearchProductComponent,
        resolve: {
            entity: CustomerResolve
        },
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.customer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customer/:id/view',
        component: CustomerDetailComponent,
        resolve: {
            customer: CustomerResolve,
            invoices: CustomerInvoiceResolve,
            products: CustomerProductResolve,
            projects: CustomerProjectResolve
        },
        data: {
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
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
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
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
            authorities: ['ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
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
            authorities: ['ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.customer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
