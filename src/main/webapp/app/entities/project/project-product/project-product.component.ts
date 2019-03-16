import { Component, OnInit } from '@angular/core';
import { IProjectProduct } from 'app/shared/model/project-product.model';
import { IProject } from 'app/shared/model/project.model';
import { ProductService } from 'app/entities/product';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectProductService } from './project-product.service';
import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ProjectService } from '../project.service';

@Component({
    selector: 'jhi-project-product',
    templateUrl: './project-product.component.html',
    styles: []
})
export class ProjectProductComponent implements OnInit {
    currentAccount: any;
    projectProducts: IProjectProduct[];
    project: IProject;
    routeData: any;

    constructor(
        protected productService: ProductService,
        protected projectProductService: ProjectProductService,
        protected projectService: ProjectService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected eventManager: JhiEventManager
    ) {
        this.routeData = this.activatedRoute.data.subscribe(data => {
            // this.page = data.pagingParams.page;
            // this.previousPage = data.pagingParams.page;
            // this.reverse = data.pagingParams.ascending;
            // this.predicate = data.pagingParams.predicate;
            this.project = data.project;
            this.projectProducts = data.projectProducts;
        });
    }

    loadAll() {}

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
    }

    update(product: IProjectProduct) {
        this.subscribeToSaveResponse(this.projectProductService.update(product));
    }

    delete(product: IProjectProduct) {
        this.subscribeToSaveResponse(this.projectProductService.delete(product.id));
    }

    saveAll() {
        this.projectProductService.batchUpdate(this.projectProducts).subscribe(
            res => {
                this.reloadProjectProduct();
            },
            err => {
                this.onError(err.errorMessage);
            }
        );
    }

    isProductEmpty(): boolean {
        if (this.projectProducts && this.projectProducts.length > 0 && this.projectProducts != null) {
            return false;
        }
        return true;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjectProduct>>) {
        result.subscribe(
            (res: HttpResponse<IProjectProduct>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError(res.message)
        );
    }

    protected onSaveError(errorMessage: string): void {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    protected onSaveSuccess(): void {
        this.reloadProjectProduct();
    }

    protected reloadProjectProduct(): any {
        this.projectProductService.findByProject(this.project.id).subscribe(
            res => {
                this.projectProducts = res.body;
            },
            err => this.onError(err.message)
        );
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
