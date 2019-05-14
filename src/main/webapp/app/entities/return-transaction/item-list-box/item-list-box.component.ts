import { Component, OnInit, Input } from '@angular/core';
import { IReturnTransaction } from 'app/shared/model/return-transaction.model';

@Component({
    selector: 'jhi-item-list-box',
    templateUrl: './item-list-box.component.html',
    styles: []
})
export class ItemListBoxComponent implements OnInit {
    // tslint:disable-next-line: no-input-rename
    @Input('returnT') returnToko: IReturnTransaction;

    constructor() {}

    ngOnInit() {}

    save() {}
}
