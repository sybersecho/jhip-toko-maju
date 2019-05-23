import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { GeraiConfig } from 'app/shared/model/gerai-config.model';
import { GeraiConfigService } from './gerai-config.service';
import { GeraiConfigUpdateComponent } from './gerai-config-update.component';
import { IGeraiConfig } from 'app/shared/model/gerai-config.model';

@Injectable({ providedIn: 'root' })
export class GeraiConfigResolve implements Resolve<IGeraiConfig> {
    constructor(private service: GeraiConfigService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGeraiConfig> {
        return this.service.find(1).pipe(
            filter((response: HttpResponse<GeraiConfig>) => response.ok),
            map((geraiConfig: HttpResponse<GeraiConfig>) => geraiConfig.body)
        );
    }
}

export const geraiConfigRoute: Routes = [
    {
        path: '',
        component: GeraiConfigUpdateComponent,
        resolve: {
            geraiConfig: GeraiConfigResolve
        },
        data: {
            authorities: ['ROLE_SUPERUSER'],
            pageTitle: 'jhiptokomajuApp.geraiConfig.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
