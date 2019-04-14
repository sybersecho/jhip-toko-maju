import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import * as moment from 'moment';
import { Moment } from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared';
import { InvoiceService } from './invoice.service';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { IInvoice } from 'app/shared/model/invoice.model';

@Component({
    selector: 'jhi-search-invoice',
    templateUrl: './search-invoice.component.html',
    styles: []
})
export class SearchInvoiceComponent implements OnInit {
    search: SearchInvoice;
    startDate: string;
    endDate: string;
    @Output() searchEvent = new EventEmitter();
    @Output() clearEvent = new EventEmitter();

    constructor() {}

    private initSearch() {
        this.search = new SearchInvoice();
        this.startDate = this.search.startDate.format(DATE_FORMAT);
        this.endDate = this.search.endDate.format(DATE_FORMAT);
    }

    ngOnInit() {
        this.initSearch();
    }

    searchInvoice(): void {
        this.search.startDate = this.startDate != null ? moment(this.startDate, DATE_TIME_FORMAT) : null;
        this.search.endDate = this.endDate != null ? moment(this.endDate, DATE_TIME_FORMAT) : null;
        this.searchEvent.emit(this.search);
    }

    clearInvoice(): void {
        this.initSearch();
        this.clearEvent.emit(this.search);
    }
}

export class SearchInvoice {
    public startDate: Moment;
    public endDate: Moment;

    constructor() {
        this.startDate = moment(new Date(), DATE_TIME_FORMAT);
        this.endDate = moment(new Date(), DATE_TIME_FORMAT);
    }
}
