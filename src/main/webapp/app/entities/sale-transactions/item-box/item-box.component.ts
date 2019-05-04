import { Component, OnInit, Input, Output, AfterViewInit, EventEmitter } from '@angular/core';
import { ISaleTransactions, SaleTransactions } from 'app/shared/model/sale-transactions.model';
import moment = require('moment');
import { SaleTransactionsService } from '../sale-transactions.service';
import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { ICustomer } from 'app/shared/model/customer.model';
import { Router } from '@angular/router';

@Component({
    selector: 'jhi-item-box',
    templateUrl: './item-box.component.html',
    styles: []
})
export class ItemBoxComponent implements OnInit, AfterViewInit {
    // tslint:disable-next-line: no-input-rename
    @Input('sale') saleTransactions: ISaleTransactions;
    // tslint:disable-next-line: no-input-rename
    @Input('customer') customer: ICustomer;
    // tslint:disable-next-line: no-output-rename
    @Output('projectChange') projectChangedEvent = new EventEmitter();
    printAsOrder = false;
    // tslint:disable-next-line: no-input-rename
    @Input('project') selectedProjectId: number;
    defaultCustomer: ICustomer;

    constructor(
        protected saleService: SaleTransactionsService,
        protected jhiAlertService: JhiAlertService,
        protected jhiEventManager: JhiEventManager,
        protected router: Router
    ) {}

    ngOnInit() {}

    ngAfterViewInit(): void {
        this.defaultCustomer = this.customer;
    }

    onPrint() {
        this.router.navigate(['/', { outlets: { print: 'sale/print/' + this.saleTransactions.noInvoice } }]);
    }

    processAsOrders() {
        this.printAsOrder = true;
        this.save();
    }

    onDeleteItem(itemPos: number) {
        this.saleTransactions.removeItemAt(itemPos);
        // this.addSaleIntoSession();
    }

    onChangeQuantity(i: number, itemQuantity: number) {
        if (itemQuantity <= 0) {
            this.onDeleteItem(i);
        } else {
            this.saleTransactions.updateItemQuantity(i, itemQuantity);
        }
        // this.addSaleIntoSession();
    }

    onPaid() {
        this.saleTransactions.paidTransaction();
        // this.addSaleIntoSession();
    }

    totalChange(): number {
        return this.saleTransactions.changes();
    }

    save() {
        this.saleTransactions.saleDate = moment(new Date());
        this.saleTransactions.projectId = this.selectedProjectId > 0 ? this.selectedProjectId : null;
        this.saleTransactions.recalculate();
        console.log('sale before save ', this.saleTransactions);
        this.subscribeToSaveResponse(this.saleService.create(this.saleTransactions));
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISaleTransactions>>) {
        result.subscribe(
            (res: HttpResponse<ISaleTransactions>) => this.onSaveSuccess(res.body),
            (res: HttpErrorResponse) => this.onSaveError(res.message)
        );
    }

    protected onSaveSuccess(sale: ISaleTransactions) {
        this.saleTransactions = sale;
        if (this.printAsOrder) {
            this.onPrint();
        }
        this.saleTransactions = new SaleTransactions();
        this.customer = this.defaultCustomer;
        this.saleTransactions.setCustomer(this.customer);
        this.selectedProjectId = 0;
        console.log('sale when save, ', this.saleTransactions);
        this.projectChangedEvent.next(this.selectedProjectId);
        this.jhiEventManager.broadcast({
            name: 'onSaveSale',
            content: this.saleTransactions
        });
        // this.addSaleIntoSession();
    }

    protected onSaveError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
