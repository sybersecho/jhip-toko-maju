import { Component, OnInit, OnDestroy, AfterContentChecked, AfterViewInit, HostListener } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ISaleTransactions, SaleTransactions } from 'app/shared/model/sale-transactions.model';
import { SaleCartService } from './sale-cart.service';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-main-cashier',
    templateUrl: './main-cashier.component.html',
    styles: []
})
export class MainCashierComponent implements OnInit, OnDestroy, AfterViewInit {
    customer: ICustomer;
    defaultCustomer: ICustomer;
    saleTransactions: ISaleTransactions = new SaleTransactions();
    selectedProjectId: number;
    routeData: any;
    isSale = false;
    eventSubcriptions: Subscription;

    constructor(
        protected activatedRoute: ActivatedRoute,
        protected cartService: SaleCartService,
        protected jhiEventManager: JhiEventManager
    ) {
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.customer = data.customer;
            this.defaultCustomer = this.customer;
        });

        this.selectedProjectId = this.cartService.getProject();
        this.saleTransactions = this.cartService.get();

        // this.getSaleInSession();
        if (!this.saleTransactions.customer) {
            this.setSaleCustomer();
        }

        // this.cartService.setSale(this.saleTransactions);
    }

    @HostListener('window:keypress', ['$event'])
    keyPressEvent(event: KeyboardEvent) {}

    ngAfterViewInit(): void {}

    ngOnInit() {
        this.registerEvent();
    }

    protected registerEvent() {
        this.eventSubcriptions = this.jhiEventManager.subscribe('onSaveSale', res => {
            this.saleTransactions = res.content;
        });
    }

    projectChange(project): void {
        this.selectedProjectId = project;
    }

    ngOnDestroy() {
        this.jhiEventManager.destroy(this.eventSubcriptions);
        this.cartService.setSale(this.saleTransactions);
        this.cartService.setProject(this.selectedProjectId);
    }

    protected setSaleCustomer() {
        this.saleTransactions.setCustomer(this.customer);
    }
}
