import { Component, OnInit, Injectable, OnDestroy } from '@angular/core';
import { NgbActiveModal, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CustomerService } from 'app/entities/customer';
import { ICustomer } from 'app/shared/model/customer.model';
import { HttpResponse } from '@angular/common/http';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-search-customer-dialog',
    templateUrl: './search-customer-dialog.component.html',
    styles: []
})
export class SearchCustomerDialogComponent implements OnInit {
    customers: ICustomer[];
    searchKeyword: string;

    constructor(public activeModal: NgbActiveModal, protected customerService: CustomerService, protected eventManager: JhiEventManager) {
        this.customers = [];
    }

    ngOnInit() {}

    search() {
        this.loadCustomer();
    }

    clearSearch() {
        this.searchKeyword = '';
        this.customers = [];
    }

    onSelect(customer: ICustomer) {
        this.eventManager.broadcast({
            name: 'onSearchSelectCustomerEvent',
            data: customer
        });
        this.activeModal.dismiss(true);
    }

    protected loadCustomer() {
        if (!this.searchKeyword) {
            this.customers = [];
            return;
        }

        this.customerService.searchByName({ query: this.searchKeyword }).subscribe(
            (res: HttpResponse<ICustomer[]>) => {
                this.customers = [];
                this.customers = res.body;
            },
            error => {
                console.log(error.message);
                this.customers = [];
            }
        );
    }
}

@Injectable({
    providedIn: 'root'
})
export class SearchCustomerDialogService implements OnDestroy {
    private isOpen = false;
    protected ngbModalRef: NgbModalRef;

    constructor(private modalService: NgbModal) {}

    open() {
        if (this.isOpen) {
            return;
        }

        this.isOpen = true;
        this.ngbModalRef = this.modalService.open(SearchCustomerDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return this.ngbModalRef;
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
