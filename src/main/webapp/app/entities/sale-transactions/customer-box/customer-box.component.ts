import { Component, OnInit, Input, OnDestroy, AfterViewInit, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { ISaleTransactions } from 'app/shared/model/sale-transactions.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { IProject, Project } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
    selector: 'jhi-customer-box',
    templateUrl: './customer-box.component.html',
    styles: []
})
export class CustomerBoxComponent implements OnInit, OnDestroy, OnChanges {
    // tslint:disable-next-line: no-input-rename
    @Input('sale') saleTransactions: ISaleTransactions;
    // tslint:disable-next-line: no-input-rename
    @Input('project') selectedProjectId: number;
    // tslint:disable-next-line: no-output-rename
    @Output('projectChange') projectChangedEvent = new EventEmitter();
    customerProjects: IProject[];
    eventSubscription: Subscription;

    constructor(protected eventManager: JhiEventManager, protected projectService: ProjectService) {
        this.customerProjects = [];
    }

    ngOnInit() {
        this.registerEvent();
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubscription);
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.loadProjects();
    }

    onChangeProject() {
        this.projectChangedEvent.next(this.selectedProjectId);
    }

    protected loadProjects(): void {
        this.customerProjects.push(this.dummyProject());
        if (!this.saleTransactions || !this.saleTransactions.customer) {
            return;
        }
        this.projectService.queryCustomerProject({ customerId: this.saleTransactions.customer.id }).subscribe(response => {
            this.customerProjects = [];
            this.customerProjects.push(this.dummyProject());
            response.body.forEach(a => {
                this.customerProjects.push(a);
            });
        });
    }

    protected dummyProject(): IProject {
        const project: IProject = new Project();
        project.id = 0;
        project.code = '';
        project.name = '';
        return project;
    }

    getCustomerCode(): string {
        return this.saleTransactions.customerCode;
    }

    getCustomerFullName(): string {
        return this.saleTransactions.customerFirstName + ' ' + this.saleTransactions.customerLastName;
    }

    protected registerEvent() {
        this.eventSubscription = this.eventManager.subscribe('onSelectCustomerEvent', response => {
            this.saleTransactions.setCustomer(response.data);
            this.loadProjects();
            this.selectedProjectId = 0;
        });
    }
}
