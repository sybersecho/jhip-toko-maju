import { Component, OnInit, Input, Output, AfterViewInit, EventEmitter, HostListener, ElementRef, ViewChild } from '@angular/core';
import { ISaleTransactions, SaleTransactions } from 'app/shared/model/sale-transactions.model';
import * as moment from 'moment';
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
    @ViewChild('discount') discountField: ElementRef;
    @ViewChild('paid') paidField: ElementRef;

    constructor(
        protected saleService: SaleTransactionsService,
        protected jhiAlertService: JhiAlertService,
        protected jhiEventManager: JhiEventManager,
        protected router: Router
    ) {}

    ngOnInit() {}

    @HostListener('window:keypress', ['$event'])
    keyPressEvent(event: KeyboardEvent) {
        // ctrl d
        if (event.ctrlKey && event.keyCode === 4) {
            this.discountField.nativeElement.focus();
        } else if (event.ctrlKey && event.keyCode === 16 && !event.shiftKey) {
            // ctrl p
            this.paidField.nativeElement.focus();
        } else if (event.ctrlKey && event.shiftKey && event.keyCode === 16 && this.saleTransactions.items.length > 0) {
            // ctrl shift p
            this.save();
        } else if (event.ctrlKey && event.shiftKey && event.keyCode === 9 && this.saleTransactions.items.length > 0) {
            // ctrl shift p
            this.processAsOrders();
        }
    }

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
