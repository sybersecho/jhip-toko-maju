import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReturnItem } from 'app/shared/model/return-item.model';

@Component({
    selector: 'jhi-return-item-detail',
    templateUrl: './return-item-detail.component.html'
})
export class ReturnItemDetailComponent implements OnInit {
    returnItem: IReturnItem;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ returnItem }) => {
            this.returnItem = returnItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
