import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IProject } from 'app/shared/model/project.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { ProjectService } from './project.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProjectProduct, IProjectProduct } from 'app/shared/model/project-product.model';
import { ProjectProductService } from './project-product/project-product.service';

@Component({
    selector: 'jhi-project',
    templateUrl: './project.component.html'
})
export class ProjectComponent implements OnInit, OnDestroy {
    currentAccount: any;
    projects: IProject[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    addProductEventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    projectId: number;

    constructor(
        protected projectService: ProjectService,
        protected projectProductService: ProjectProductService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.projectService
                .search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IProject[]>) => this.paginateProjects(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.projectService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IProject[]>) => this.paginateProjects(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/project'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            '/project',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            '/project',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInProjects();
        this.registerAddProduct();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
        this.eventManager.destroy(this.addProductEventSubscriber);
    }

    trackId(index: number, item: IProject) {
        return item.id;
    }

    registerChangeInProjects() {
        this.eventSubscriber = this.eventManager.subscribe('projectListModification', response => this.loadAll());
    }

    registerAddProduct() {
        this.addProductEventSubscriber = this.eventManager.subscribe('addProduct', response => {
            console.log('add product from project');
            const project: IProject = response.entity;
            this.projectId = project.id;

            const save: IProject = this.createProjectProduct(response.content);

            this.subscribeToSaveResponse(this.projectProductService.create(save));
        });
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    onDelete(event: any) {
        console.log(event);
        event.stopPropagation();
        this.router.navigate(['/', { outlets: { popup: 'project/' + 6 + '/delete' } }], { replaceUrl: true, queryParamsHandling: 'merge' });

        // this.navigate
        // onDelete
        // [routerLink]="['/', { outlets: { popup: 'project/' + project.id + '/delete' } }]"
    }

    protected createProjectProduct(product: IProduct): IProject {
        const aProjectProduct = new ProjectProduct();
        aProjectProduct.productId = product.id;
        aProjectProduct.specialPrice = product.sellingPrice;
        aProjectProduct.projectId = this.projectId;

        return aProjectProduct;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjectProduct>>) {
        result.subscribe(
            (res: HttpResponse<IProjectProduct>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    protected onSaveSuccess(): void {
        // throw new Error("Method not implemented.");
    }

    protected paginateProjects(data: IProject[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.projects = data;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
