import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StockOrder } from 'app/shared/model/stock-order.model';
import { StockOrderService } from './stock-order.service';
import { StockOrderComponent } from './stock-order.component';
import { IStockOrder } from 'app/shared/model/stock-order.model';
import { StockOrderInputComponent } from './stock-order-input.component';
import { StockOrderProcessComponent } from './stock-order-process.component';

@Injectable({ providedIn: 'root' })
export class StockOrderResolve implements Resolve<IStockOrder> {
    constructor(private service: StockOrderService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStockOrder> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<StockOrder>) => response.ok),
                map((stockOrder: HttpResponse<StockOrder>) => stockOrder.body)
            );
        }
        return of(new StockOrder());
    }
}

export const stockOrderRoute: Routes = [
    {
        path: '',
        component: StockOrderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.stockOrder.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'input',
        component: StockOrderInputComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.stockOrder.input.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'process',
        component: StockOrderProcessComponent,
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.stockOrder.process.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
