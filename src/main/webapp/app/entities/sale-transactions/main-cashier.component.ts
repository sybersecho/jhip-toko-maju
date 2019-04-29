import { Component, OnInit, OnDestroy, AfterContentChecked, AfterViewInit } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';
import { JhiParseLinks, JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ISaleTransactions, SaleTransactions } from 'app/shared/model/sale-transactions.model';
import { Subscription, Observable } from 'rxjs';
import { ISaleItem } from 'app/shared/model/sale-item.model';
import { SaleTransactionsService } from './sale-transactions.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import * as moment from 'moment';
import { SaleCartService } from './sale-cart.service';
import { CustomerService } from '../customer';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { ProjectService } from '../project';
import { IProject, Project } from 'app/shared/model/project.model';

@Component({
    selector: 'jhi-main-cashier',
    templateUrl: './main-cashier.component.html',
    styles: []
})
export class MainCashierComponent implements OnInit, OnDestroy {
    customer: ICustomer;
    defaultCustomer: ICustomer;
    saleTransactions: ISaleTransactions = new SaleTransactions();
    customerProducts: ICustomerProduct[];
    customerProjects: IProject[];
    selectedProjectId: number;
    currentAccount: any;
    routeData: any;
    addItemESubcriber: Subscription;
    isSale = false;
    printAsOrder = false;

    constructor(
        protected saleService: SaleTransactionsService,
        protected customerService: CustomerService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router,
        protected eventManager: JhiEventManager,
        protected cartService: SaleCartService
    ) {
        this.routeData = this.activatedRoute.data.subscribe(data => {
            // this.page = data.pagingParams.page;
            // this.previousPage = data.pagingParams.page;
            // this.reverse = data.pagingParams.ascending;
            // this.predicate = data.pagingParams.predicate;
            this.customer = data.customer;
            this.defaultCustomer = this.customer;
        });
        this.customerProjects = [];
        this.selectedProjectId = this.cartService.getProject();
        this.saleTransactions = this.cartService.get();

        // this.saleTransactions.setSaleService(this.saleService);
        // this.getSaleInSession();
        if (!this.saleTransactions.customer) {
            this.setSaleCustomer();
        }

        // this.cartService.setSale(this.saleTransactions);
    }

    ngOnInit() {
        // this.customerProjects = [];
        this.registerAddItemEvent();
        this.changeCustomerEvent();
        this.loadCustomerProduct();
        // this.loadCustomerProject();
        // this.getSaleInSession();
    }

    onPrint() {
        // this.router.navigate(['/', { outlets: { print: 'sale/print/' + sale.noInvoice } }]);
    }

    // customerChange(sale: ISaleTransactions) {
    //     console.log('before');
    //     console.log(this.saleTransactions);
    // this.saleTransactions = sale;
    // console.log('after');
    // console.log(this.saleTransactions);
    // }

    processAsOrders() {
        this.printAsOrder = true;
        this.save();
    }

    projectChange(project): void {
        this.selectedProjectId = project;
    }

    // getCustomerCode(): string {
    //     return this.saleTransactions.customerCode;
    // }

    // getCustomerFullName(): string {
    //     return this.saleTransactions.customerFirstName + ' ' + this.saleTransactions.customerLastName;
    // }

    getCustomerAddress(): string {
        return this.saleTransactions.customerAddress;
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.addItemESubcriber);
        // this.eventManager.destroy(this.changeCustomerSubcriber);
        this.cartService.setSale(this.saleTransactions);
        this.cartService.setProject(this.selectedProjectId);
    }

    getFullName(): string {
        return this.customer.firstName + ' ' + this.customer.lastName;
    }

    save() {
        this.saleTransactions.saleDate = moment(new Date());
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
        // console.log(sale);
        if (this.printAsOrder) {
            // this.onPrint(sale);
        }
        this.saleTransactions = new SaleTransactions();
        this.customer = this.defaultCustomer;
        this.eventManager.broadcast({ name: 'saleSavedEvent' });
        this.saleTransactions.setCustomer(this.customer);
        this.selectedProjectId = 0;
        // this.addSaleIntoSession();
    }

    protected onSaveError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
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

    onSearchCustomer() {
        // console.log('on search customer');
    }

    totalChange(): number {
        return this.saleTransactions.changes();
    }

    protected setSaleCustomer() {
        // this.customerProjects = [];
        this.loadCustomerProduct();
        this.saleTransactions.setCustomer(this.customer);
        // this.loadCustomerProject();

        // this.addSaleIntoSession();
    }

    // private getSaleInSession() {
    //     this.saleService.getInSession().subscribe(
    //         res => {
    //             console.log('response from get sale in session!!!');
    //             console.log(res);
    //             this.saleTransactions = res.body;
    //         },
    //         err => {
    //             console.log('Error: ' + err.message);
    //         }
    //     );
    // }

    // private addSaleIntoSession() {
    //     if (this.saleTransactions) {
    //         this.saleService.addToSession(this.saleTransactions).subscribe(res => {
    //             console.log('success call add to session');
    //         });
    //     }
    // }

    protected registerAddItemEvent(): any {
        this.addItemESubcriber = this.eventManager.subscribe('addItemEvent', response => {
            const item: ISaleItem = response.item;
            const price = this.getCustomerProductPrice(item.productId);
            item.sellingPrice = price > 0 ? price : item.sellingPrice;
            item.createItem();
            this.saleTransactions.addOrUpdate(item);
        });
    }

    private getCustomerProductPrice(productId: number): number {
        const find: ICustomerProduct = this.findItemInCustomerProductList(productId);
        return find ? find.specialPrice : -1;
    }

    private findItemInCustomerProductList(productId: number): ICustomerProduct {
        let cusItem: ICustomerProduct = null;
        if (this.customerProducts && this.customerProducts.length > 0) {
            cusItem = this.customerProducts.find(product => product.productId === productId);
        }
        return cusItem;
    }

    // loadCustomerProject(): void {
    // this.customerProjects = [];
    // console.log(this.customerProjects);
    // this.customerProjects.push(this.dummyProject());
    // if (!this.customer || !this.saleTransactions || !this.saleTransactions.customer) {
    //     return;
    // }
    // this.projectService.queryCustomerProject({ customerId: this.saleTransactions.customer.id }).subscribe(response => {
    //     // console.log('in ');
    //     // console.log(this.customerProjects);
    //     this.customerProjects = [];
    //     this.customerProjects.push(this.dummyProject());
    //     response.body.forEach(a => {
    //         this.customerProjects.push(a);
    //     });
    // });
    // }

    protected dummyProject(): IProject {
        const project: IProject = new Project();
        project.id = 0;
        project.code = '';
        project.name = '';
        return project;
    }

    loadCustomerProduct(): void {
        this.customerService.searcyByCustomer(this.customer.id).subscribe(
            response => {
                this.customerProducts = response.body;
                // console.log(this.customerProducts);
                // this.updateExistingItemPrice();
            },
            err => {
                console.error(err.message);
            }
        );
    }

    protected changeCustomerEvent(): any {
        // this.changeCustomerSubcriber = this.eventManager.subscribe('onSelectCustomerEvent', response => {
        //     this.customer = response.data;
        //     this.setSaleCustomer();
        // });
    }

    protected updateExistingItemPrice(): void {
        this.saleTransactions.items.forEach(existingItem => {
            // console.log('Sell Price Before: ' + existingItem.sellingPrice);
            const found = this.getCustomerProductPrice(existingItem.productId);

            existingItem.sellingPrice = found > 0 ? found : existingItem.product.sellingPrice;

            existingItem.createItem();
        });
        this.saleTransactions.recalculate();
    }

    protected push(item: ISaleItem): void {
        this.saleTransactions.addOrUpdate(item);
    }
}
