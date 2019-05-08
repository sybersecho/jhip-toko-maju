import { Component, OnInit, OnDestroy } from '@angular/core';
import { ExtractProductModel } from './extract-product-model';
import { SearcExtProductDialogService } from './search-ext-product.component';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { ExcelService } from 'app/shared/export/excel.service';
import { ExcelModel } from 'app/shared/export/excel-model';

@Component({
    selector: 'jhi-extract-product',
    templateUrl: './extract-product.component.html',
    styles: []
})
export class ExtractProductComponent implements OnInit, OnDestroy {
    products: ExtractProductModel[];
    eventSubcriptions: Subscription;
    isExistInList = false;

    constructor(
        protected searchServiceDialog: SearcExtProductDialogService,
        protected eventManager: JhiEventManager,
        protected jhiAlertService: JhiAlertService,
        protected excelService: ExcelService
    ) {
        this.products = [];
    }

    ngOnInit() {
        this.registerEvent();
    }

    registerEvent() {
        this.eventSubcriptions = this.eventManager.subscribe('onExtractProductEvt', response => {
            this.checkAndAdd(response.data);
            this.notifyIfExist();

            // this.products.push(;
        });
        this.eventSubcriptions = this.eventManager.subscribe('onExtractProductBySupplierEvt', response => {
            const dataRes: ExtractProductModel[] = response.data;
            dataRes.forEach(d => {
                this.checkAndAdd(d);
            });
            this.notifyIfExist();
        });
    }

    private notifyIfExist() {
        if (this.isExistInList) {
            this.onError('jhiptokomajuApp.product.extract.messages.product.exist');
            this.isExistInList = false;
        }
    }

    protected checkAndAdd(data: ExtractProductModel) {
        const exist = this.checkIfExist(data);
        exist === -1 ? this.products.push(data) : (this.isExistInList = true);
    }

    protected checkIfExist(data: ExtractProductModel): number {
        return this.products.findIndex(f => f.barcode === data.barcode);
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    remove(i) {
        this.products.splice(i, 1);
    }

    onSearch() {
        this.searchServiceDialog.open();
    }

    onExtract() {
        const excelModel = new ExcelModel();
        excelModel.data = this.products;
        excelModel.fileName = 'Extract Product';
        excelModel.header = [];
        excelModel.title = '';

        this.excelService.generateExcel(excelModel);
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubcriptions);
    }
}
