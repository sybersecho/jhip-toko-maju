import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IGeraiConfig } from 'app/shared/model/gerai-config.model';
import { GeraiConfigService } from './gerai-config.service';

@Component({
    selector: 'jhi-gerai-config-update',
    templateUrl: './gerai-config-update.component.html'
})
export class GeraiConfigUpdateComponent implements OnInit {
    geraiConfig: IGeraiConfig;
    isSaving: boolean;

    constructor(protected geraiConfigService: GeraiConfigService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ geraiConfig }) => {
            this.geraiConfig = geraiConfig;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.geraiConfig.id !== undefined) {
            this.subscribeToSaveResponse(this.geraiConfigService.update(this.geraiConfig));
        } else {
            this.subscribeToSaveResponse(this.geraiConfigService.create(this.geraiConfig));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IGeraiConfig>>) {
        result.subscribe((res: HttpResponse<IGeraiConfig>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        // this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
