import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SupplierStock } from 'app/shared/model/supplier-stock.model';
import { SupplierStockComponent } from './supplier-stock.component';
import { ISupplierStock } from 'app/shared/model/supplier-stock.model';

export const supplierStockRoute: Routes = [
    {
        path: 'supplier-stock',
        component: SupplierStockComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.supplierStock.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
