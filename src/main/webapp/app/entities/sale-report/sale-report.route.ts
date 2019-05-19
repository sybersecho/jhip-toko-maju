import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { SaleReportComponent } from './sale-report.component';
import { SaleReportCustomerComponent } from './sale-report-customer.component';
import { SaleReportProductComponent } from './sale-report-product.component';

export const saleReportRoute: Routes = [
    {
        path: 'detail',
        component: SaleReportComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.saleReport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'customer',
        component: SaleReportCustomerComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'jhiptokomajuApp.saleReport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'product',
        component: SaleReportProductComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'jhiptokomajuApp.saleReport.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
