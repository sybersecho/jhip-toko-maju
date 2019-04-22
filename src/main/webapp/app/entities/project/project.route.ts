import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Project } from 'app/shared/model/project.model';
import { ProjectService } from './project.service';
import { ProjectComponent } from './project.component';
import { ProjectDetailComponent } from './project-detail.component';
import { ProjectUpdateComponent } from './project-update.component';
import { ProjectDeletePopupComponent } from './project-delete-dialog.component';
import { IProject } from 'app/shared/model/project.model';
import { InfoProductComponent } from '../customer';
import { SearchProductComponent } from '../product';
import { ProjectProductComponent } from './project-product/project-product.component';
import { ProjectProductResolve } from './project-product/project-product.resolve';

@Injectable({ providedIn: 'root' })
export class ProjectResolve implements Resolve<IProject> {
    constructor(private service: ProjectService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProject> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Project>) => response.ok),
                map((project: HttpResponse<Project>) => project.body)
            );
        }
        return of(new Project());
    }
}

export const projectRoute: Routes = [
    {
        path: 'project',
        component: ProjectComponent,
        // children: [
        //     {
        //         path: '',
        //         component: InfoProductComponent,
        //         data: {
        //             authorities: ['ROLE_USER'],
        //             defaultSort: 'id,asc',
        //             pageTitle: 'jhiptokomajuApp.project.home.title'
        //         }
        //     },
        //     {
        //         path: ':id/products',
        //         component: ProjectProductComponent,
        //         resolve: {
        //             projectProducts: ProjectProductResolve,
        //             project: ProjectResolve
        //         },
        //         data: {
        //             authorities: ['ROLE_USER'],
        //             defaultSort: 'id,asc',
        //             pageTitle: 'jhiptokomajuApp.project.home.title'
        //         }
        //     },
        //     {
        //         path: ':id/search-product',
        //         component: SearchProductComponent,
        //         resolve: {
        //             entity: ProjectResolve
        //         },
        //         data: {
        //             authorities: ['ROLE_USER'],
        //             pageTitle: 'jhiptokomajuApp.project.home.title'
        //         },
        //         canActivate: [UserRouteAccessService]
        //     }
        // ],
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'jhiptokomajuApp.project.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    // {
    //     path: 'project/:id',
    //     component: ProjectComponent,
    //     children: [
    //         // {
    //         //     path: '',
    //         //     component: InfoProductComponent,
    //         //     data: {
    //         //         authorities: ['ROLE_USER'],
    //         //         defaultSort: 'id,asc',
    //         //         pageTitle: 'jhiptokomajuApp.project.home.title'
    //         //     }
    //         // },
    //         {
    //             path: '',
    //             component: ProjectProductComponent,
    //             resolve: {
    //                 projectProducts: ProjectProductResolve,
    //                 project: ProjectResolve
    //             },
    //             data: {
    //                 authorities: ['ROLE_USER'],
    //                 defaultSort: 'id,asc',
    //                 pageTitle: 'jhiptokomajuApp.project.home.title'
    //             }
    //         },
    //         {
    //             path: 'search-product',
    //             component: SearchProductComponent,
    //             resolve: {
    //                 entity: ProjectResolve
    //             },
    //             data: {
    //                 authorities: ['ROLE_USER'],
    //                 pageTitle: 'jhiptokomajuApp.project.home.title'
    //             },
    //             canActivate: [UserRouteAccessService]
    //         }
    //     ],
    //     resolve: {
    //         pagingParams: JhiResolvePagingParams
    //     },
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         defaultSort: 'id,asc',
    //         pageTitle: 'jhiptokomajuApp.project.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // },
    {
        path: 'project/:id/view',
        component: ProjectDetailComponent,
        resolve: {
            project: ProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.project.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project/new',
        component: ProjectUpdateComponent,
        resolve: {
            project: ProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.project.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project/:id/edit',
        component: ProjectUpdateComponent,
        resolve: {
            project: ProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.project.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectPopupRoute: Routes = [
    {
        path: 'project/:id/delete',
        component: ProjectDeletePopupComponent,
        resolve: {
            project: ProjectResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhiptokomajuApp.project.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
