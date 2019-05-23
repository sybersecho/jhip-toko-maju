import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Gerai } from 'app/shared/model/gerai.model';
import { GeraiService } from './gerai.service';
import { GeraiComponent } from './gerai.component';
import { GeraiDetailComponent } from './gerai-detail.component';
import { GeraiUpdateComponent } from './gerai-update.component';
import { GeraiDeletePopupComponent } from './gerai-delete-dialog.component';
import { IGerai } from 'app/shared/model/gerai.model';

@Injectable({ providedIn: 'root' })
export class GeraiResolve implements Resolve<IGerai> {
    constructor(private service: GeraiService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGerai> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Gerai>) => response.ok),
                map((gerai: HttpResponse<Gerai>) => gerai.body)
            );
        }
        return of(new Gerai());
    }
}

export const geraiRoute: Routes = [
    {
        path: '',
        component: GeraiComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_SUPERUSER'],
            defaultSort: 'id,asc',
            pageTitle: 'jhiptokomajuApp.gerai.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: GeraiDetailComponent,
        resolve: {
            gerai: GeraiResolve
        },
        data: {
            authorities: ['ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.gerai.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: GeraiUpdateComponent,
        resolve: {
            gerai: GeraiResolve
        },
        data: {
            authorities: ['ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.gerai.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: GeraiUpdateComponent,
        resolve: {
            gerai: GeraiResolve
        },
        data: {
            authorities: ['ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.gerai.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const geraiPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: GeraiDeletePopupComponent,
        resolve: {
            gerai: GeraiResolve
        },
        data: {
            authorities: ['ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.gerai.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
