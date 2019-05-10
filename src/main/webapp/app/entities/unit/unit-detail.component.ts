import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUnit } from 'app/shared/model/unit.model';

@Component({
    selector: 'jhi-unit-detail',
    templateUrl: './unit-detail.component.html'
})
export class UnitDetailComponent implements OnInit {
    unit: IUnit;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ unit }) => {
            this.unit = unit;
        });
    }

    previousState() {
        window.history.back();
    }
}
