import { Component, OnInit, Injectable } from '@angular/core';
import { NgbModalRef, NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IInvoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from 'app/entities/invoice';
import { ISaleItem } from 'app/shared/model/sale-item.model';
import { IDuePayment } from 'app/shared/model/due-payment.model';

@Component({
    selector: 'jhi-invoice-detail-dialog',
    templateUrl: './invoice-detail-dialog.component.html',
    styles: []
})
export class InvoiceDetailDialogComponent implements OnInit {
    invoice: IInvoice;
    items: ISaleItem[];
    payments: IDuePayment[];
    constructor(public activeModal: NgbActiveModal, protected invoiceService: InvoiceService) {
        this.items = [];
        this.payments = [];
    }

    ngOnInit() {
        this.loadInvoiceItems();
        this.loadInvoiceHistory();
    }

    loadInvoiceHistory() {
        return this.invoiceService.queryInvoiceHistory({ saleId: this.invoice.id }).subscribe(res => {
            this.payments = res.body;
            // console.log(this.payments);
        });
    }

    loadInvoiceItems() {
        return this.invoiceService.queryInvoiceItems({ invoice: this.invoice.noInvoice }).subscribe(res => {
            this.items = res.body;
            // console.log(this.items);
        });
    }
}

@Injectable({
    providedIn: 'root'
})
export class InvoiceDetailDialogService {
    private isOpen = false;
    protected ngbModalRef: NgbModalRef;
    constructor(private modalService: NgbModal) {}

    open(invoice: IInvoice) {
        if (this.isOpen) {
            return;
        }

        this.isOpen = true;
        this.ngbModalRef = this.modalService.open(InvoiceDetailDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.invoice = invoice;
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
}
