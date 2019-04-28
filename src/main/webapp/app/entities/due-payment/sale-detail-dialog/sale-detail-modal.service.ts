import { Injectable, Component } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ISaleItem } from 'app/shared/model/sale-item.model';
import { SaleDetailDialogComponent } from './sale-detail-dialog.component';

@Injectable({
    providedIn: 'root'
})
export class SaleDetailModalService {
    private isOpen = false;
    protected ngbModalRef: NgbModalRef;
    constructor(private modalService: NgbModal) {}

    open(items: ISaleItem[]) {
        console.log('open call: ' + this.isOpen);
        if (this.isOpen) {
            return;
        }

        this.isOpen = true;
        this.ngbModalRef = this.modalService.open(SaleDetailDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.items = items;
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
