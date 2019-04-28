import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ISaleItem } from 'app/shared/model/sale-item.model';

@Component({
    selector: 'jhi-sale-detail-dialog',
    templateUrl: './sale-detail-dialog.component.html',
    styles: []
})
export class SaleDetailDialogComponent implements OnInit {
    items: ISaleItem[];
    constructor(public activeModal: NgbActiveModal) {}

    ngOnInit() {
        // console.log('items in dialog');
        // console.log(this.items);
    }
}
