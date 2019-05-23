import { Component, OnInit, AfterViewInit } from '@angular/core';
import { IGerai } from 'app/shared/model/gerai.model';
import { GeraiService } from './gerai.service';
import { HttpResponse, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { GeraiTransactionService } from './gerai-transaction.service';
import { IGeraiTransaction } from 'app/shared/model/gerai-transaction.model';

@Component({
    selector: 'jhi-gerai-overview',
    templateUrl: './gerai-overview.component.html',
    styles: []
})
export class GeraiOverviewComponent implements OnInit, AfterViewInit {
    selectedGerai: number;
    gerais: IGerai[];
    geraiTransactions: IGeraiTransaction[];
    predicate: any;
    reverse: any;

    constructor(
        protected geraiService: GeraiService,
        protected geraiTransactionServcie: GeraiTransactionService,
        protected jhiAlertService: JhiAlertService
    ) {
        this.gerais = [];
        this.geraiTransactions = [];
    }

    ngOnInit() {
        this.loadAll();
    }

    loadAll() {
        this.geraiService
            .query()
            .subscribe(
                (res: HttpResponse<IGerai[]>) => this.paginateGerais(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    protected paginateGerais(data: IGerai[], headers: HttpHeaders) {
        this.gerais = data;
        if (this.gerais) {
            this.selectedGerai = this.gerais[0].id;
            this.loadTransactions();
        }
    }

    onChangeGerai() {
        this.geraiTransactions = [];
        this.loadTransactions();
    }

    loadTransactions() {
        this.geraiTransactionServcie.findByGeraiId(this.selectedGerai).subscribe(
            res => {
                console.log('success, ', res.body);
                this.geraiTransactions = res.body;
            },
            error => {
                this.onError(error.message);
            }
        );
    }

    protected onError(errorMessage: string) {
        console.error('message: ', errorMessage);
        this.jhiAlertService.error('error.somethingwrong', null, null);
    }

    ngAfterViewInit(): void {
        console.log('selected gerai: ', this.selectedGerai);
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
}
