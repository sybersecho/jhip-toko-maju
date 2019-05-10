import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Unit } from 'app/shared/model/unit.model';
import { UnitService } from './unit.service';
import { UnitComponent } from './unit.component';
import { UnitDetailComponent } from './unit-detail.component';
import { UnitUpdateComponent } from './unit-update.component';
import { UnitDeletePopupComponent } from './unit-delete-dialog.component';
import { IUnit } from 'app/shared/model/unit.model';

@Injectable({ providedIn: 'root' })
export class UnitResolve implements Resolve<IUnit> {
    constructor(private service: UnitService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUnit> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Unit>) => response.ok),
                map((unit: HttpResponse<Unit>) => unit.body)
            );
        }
        return of(new Unit());
    }
}

export const unitRoute: Routes = [
    {
        path: '',
        component: UnitComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'jhiptokomajuApp.unit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: UnitDetailComponent,
        resolve: {
            unit: UnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.unit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: UnitUpdateComponent,
        resolve: {
            unit: UnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.unit.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: UnitUpdateComponent,
        resolve: {
            unit: UnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.unit.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const unitPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: UnitDeletePopupComponent,
        resolve: {
            unit: UnitResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.unit.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
