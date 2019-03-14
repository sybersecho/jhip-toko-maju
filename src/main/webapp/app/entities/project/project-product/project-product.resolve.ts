import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { IProjectProduct } from 'app/shared/model/project-product.model';

import { HttpResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { ProjectProductService } from './project-product.service';

@Injectable({
    providedIn: 'root'
})
export class ProjectProductResolve implements Resolve<IProjectProduct[]> {
    constructor(private service: ProjectProductService) {}

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): IProjectProduct[] | Observable<IProjectProduct[]> | Promise<IProjectProduct[]> {
        const id = route.params['id'] ? route.params['id'] : null;
        console.log('ProjectProductResolve -: ID: ', id);
        if (id) {
            console.log('id is not null');
            return this.service.findByProject(id).pipe(
                filter((response: HttpResponse<IProjectProduct[]>) => response.ok),
                map((response: HttpResponse<IProjectProduct[]>) => response.body)
            );
        }

        return null;
    }

    // resolve
}
