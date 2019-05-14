import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { IReturnTransaction } from 'app/shared/model/return-transaction.model';

@Component({
    selector: 'jhi-item-list-box',
    templateUrl: './item-list-box.component.html',
    styles: []
})
export class ItemListBoxComponent implements OnInit, OnChanges {
    // tslint:disable-next-line: no-input-rename
    @Input('returnT') returnToko: IReturnTransaction;
    cashReturn = false;

    constructor() {}

    ngOnInit() {}

    save() {}

    ngOnChanges(changes: SimpleChanges): void {
        this.returnToko.calculateTotalReturn();
    }

    onDeleteItem(i: number) {
        this.returnToko.removeItemAt(i);
    }

    onChangeQuantity(i: number, itemQuantity: number) {
        if (itemQuantity <= 0) {
            this.onDeleteItem(i);
        } else {
            this.returnToko.updateQuantity(i, itemQuantity);
        }
        // this.addSaleIntoSession();
    }
}
