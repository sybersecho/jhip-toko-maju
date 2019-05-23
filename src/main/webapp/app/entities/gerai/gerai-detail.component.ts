import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGerai } from 'app/shared/model/gerai.model';

@Component({
    selector: 'jhi-gerai-detail',
    templateUrl: './gerai-detail.component.html'
})
export class GeraiDetailComponent implements OnInit {
    gerai: IGerai;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gerai }) => {
            this.gerai = gerai;
        });
    }

    previousState() {
        window.history.back();
    }
}
