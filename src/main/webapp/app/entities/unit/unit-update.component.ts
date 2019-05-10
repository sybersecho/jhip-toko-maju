import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IUnit } from 'app/shared/model/unit.model';
import { UnitService } from './unit.service';

@Component({
    selector: 'jhi-unit-update',
    templateUrl: './unit-update.component.html'
})
export class UnitUpdateComponent implements OnInit {
    unit: IUnit;
    isSaving: boolean;

    constructor(protected unitService: UnitService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ unit }) => {
            this.unit = unit;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.unit.id !== undefined) {
            this.subscribeToSaveResponse(this.unitService.update(this.unit));
        } else {
            this.subscribeToSaveResponse(this.unitService.create(this.unit));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IUnit>>) {
        result.subscribe((res: HttpResponse<IUnit>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
