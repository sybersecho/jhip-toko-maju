import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { ProductStockComponent } from './product-stock.component';

export const productStockRoute: Routes = [
    {
        path: 'product-stock',
        component: ProductStockComponent,
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.productStock.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

// export const productStockPopupRoute: Routes = [{}];
