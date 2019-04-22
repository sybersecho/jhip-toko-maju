import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Router, ActivatedRoute } from '@angular/router';
import { ITEMS_PER_PAGE } from 'app/shared';

@Component({
    selector: 'jhi-customer-project',
    templateUrl: './customer-project.component.html',
    styles: []
})
export class CustomerProjectComponent implements OnInit {
    // tslint:disable-next-line: no-input-rename
    @Input('customer-in') customer: ICustomer;
    @Output() countProject = new EventEmitter();
    // tslint:disable-next-line: no-input-rename
    @Input('project-in') projects: IProject[];
    links: any;
    totalItems: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        protected projectService: ProjectService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected router: Router,
        protected activatedRoute: ActivatedRoute
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
    }

    ngOnInit() {
        // this.loadCustomerProject();
    }

    loadCustomerProject() {
        this.projectService
            .query({
                // page: this.page - 1,
                customerId: this.customer.id
                // size: this.itemsPerPage,
                // sort: this.sort()
            })
            .subscribe(
                response => {
                    this.projects = response.body;
                    this.countProject.next(this.projects.length);
                },
                error => this.onError(error.errorMessage)
            );
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    trackId(index: number, item: IProject) {
        return item.id;
    }

    onError(errorMessage: any): void {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
