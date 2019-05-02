import { Component, OnInit, EventEmitter, Output, OnDestroy } from '@angular/core';
import * as moment from 'moment';
import { Moment } from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared';
import { InvoiceService } from './invoice.service';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { IInvoice } from 'app/shared/model/invoice.model';
import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { IProject, Project } from 'app/shared/model/project.model';
import { SearchCustomerDialogService } from './search-customer-dialog/search-customer-dialog.component';
import { SearchProjectDialogService } from './search-project-dialog/search-project-dialog.component';

@Component({
    selector: 'jhi-search-filter',
    templateUrl: './search-filter.component.html',
    styles: []
})
export class SearchFilterComponent implements OnInit, OnDestroy {
    searchCriteria: SearchInvoice;
    fromDate: string;
    endDate: string;
    // noInvoice: string;
    // customer: ICustomer;
    project: IProject;
    status: string;
    eventSubcriptions: Subscription;

    @Output() searchEvent = new EventEmitter();
    @Output() clearEvent = new EventEmitter();

    constructor(
        protected customerDialogService: SearchCustomerDialogService,
        protected projectDialogService: SearchProjectDialogService,
        protected eventManager: JhiEventManager,
        protected invoiceService: InvoiceService
    ) {}

    private initSearch() {
        this.searchCriteria = new SearchInvoice();
        this.fromDate = this.searchCriteria.fromDate.format(DATE_FORMAT);
        this.endDate = this.searchCriteria.endDate.format(DATE_FORMAT);
    }

    ngOnInit() {
        this.registerEvent();
        this.initSearch();
    }

    protected registerEvent() {
        this.eventSubcriptions = this.eventManager.subscribe('onSearchSelectCustomerEvent', response => {
            this.searchCriteria.customer = response.data;
        });

        this.eventSubcriptions = this.eventManager.subscribe('onSearchSelectProjectEvent', response => {
            this.searchCriteria.project = response.data;
        });
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubcriptions);
    }

    search(): void {
        this.searchCriteria.fromDate = this.fromDate != null ? moment(this.fromDate, DATE_TIME_FORMAT) : null;
        this.searchCriteria.endDate = this.endDate != null ? moment(this.endDate, DATE_TIME_FORMAT) : null;

        // const queryCriteria: any = {};
        // queryCriteria.start = moment(this.searchCriteria.fromDate)
        //     .startOf('day')
        //     .toJSON();
        // queryCriteria.end = moment(this.searchCriteria.endDate)
        //     .endOf('day')
        //     .toJSON();
        // if (this.searchCriteria.customer) {
        //     queryCriteria.customer = this.searchCriteria.customer.id;
        // }
        // if (this.searchCriteria.project) {
        //     queryCriteria.project = this.searchCriteria.project.id;
        // }
        // if (this.searchCriteria.noInvoice) {
        //     queryCriteria.invoice = this.searchCriteria.noInvoice;
        // }

        // this.invoiceService.query(queryCriteria).subscribe(
        //     (res: HttpResponse<IInvoice[]>) => {
        //         this.searchEvent.emit(res.body);
        //     },
        //     err => {
        //         console.log(err.message);
        //     }
        // );
        this.searchEvent.emit(this.searchCriteria);
    }

    clear(): void {
        this.initSearch();
        this.clearEvent.next([]);
    }

    searchCustomer() {
        // console.log('Customer, ', this.searchCriteria.customer);
        this.customerDialogService.open();
    }

    clearCustomer() {
        this.searchCriteria.customer = null;
    }

    customerFullName(): string {
        return this.searchCriteria && this.searchCriteria.customer ? this.getFullName() : null;
    }

    protected getFullName(): string {
        const fullName = this.searchCriteria.customer.firstName + ' ' + this.searchCriteria.customer.lastName;
        return fullName;
    }

    clearInvoice(): void {
        // this.initSearch();
        // this.clearEvent.emit(this.search);
        // this.noInvoice = '';
        this.searchCriteria.noInvoice = '';
    }

    projectName(): string {
        return this.searchCriteria && this.searchCriteria.project ? this.searchCriteria.project.name : null;
    }

    searchProject(): void {
        this.projectDialogService.open();
    }

    clearProject(): void {
        this.searchCriteria.project = null;
    }
}

export class SearchInvoice {
    public fromDate: Moment;
    public endDate: Moment;
    public noInvoice: string;
    public customer: ICustomer;
    public project: IProject;
    public status: string;

    constructor() {
        this.fromDate = moment(new Date(), DATE_TIME_FORMAT);
        this.endDate = moment(new Date(), DATE_TIME_FORMAT);
        this.noInvoice = '';
        this.customer = null;
        this.project = null;
        this.status = '';
    }
}
