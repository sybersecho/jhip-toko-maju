import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IGerai } from 'app/shared/model/gerai.model';
import { GeraiService } from './gerai.service';
import { IUser, UserService, AccountService } from 'app/core';

@Component({
    selector: 'jhi-gerai-update',
    templateUrl: './gerai-update.component.html'
})
export class GeraiUpdateComponent implements OnInit {
    currentAccount: any;
    gerai: IGerai;
    isSaving: boolean;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected geraiService: GeraiService,
        protected userService: UserService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ gerai }) => {
            this.gerai = gerai;
        });
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;

        if (this.gerai.id !== undefined) {
            this.subscribeToSaveResponse(this.geraiService.update(this.gerai));
        } else {
            this.gerai.createdDate = moment();
            this.gerai.creatorId = this.currentAccount.id;
            this.gerai.creatorLogin = this.currentAccount.loggin;
            this.subscribeToSaveResponse(this.geraiService.create(this.gerai));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IGerai>>) {
        result.subscribe((res: HttpResponse<IGerai>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
