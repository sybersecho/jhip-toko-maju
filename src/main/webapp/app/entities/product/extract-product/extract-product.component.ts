import { Component, OnInit, OnDestroy } from '@angular/core';
import { ExtractProductModel } from './extract-product-model';
import { SearcExtProductDialogService } from './search-ext-product.component';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-extract-product',
    templateUrl: './extract-product.component.html',
    styles: []
})
export class ExtractProductComponent implements OnInit, OnDestroy {
    products: ExtractProductModel[];
    eventSubcriptions: Subscription;

    constructor(protected searchServiceDialog: SearcExtProductDialogService, protected eventManager: JhiEventManager) {
        this.products = [];
    }

    ngOnInit() {
        this.registerEvent();
    }

    registerEvent() {
        this.eventSubcriptions = this.eventManager.subscribe('onExtractProductEvt', response => {
            this.products.push(response.data);
        });
        this.eventSubcriptions = this.eventManager.subscribe('onExtractProductBySupplierEvt', response => {
            const dataRes: ExtractProductModel[] = response.data;
            dataRes.forEach(d => {
                this.products.push(d);
            });
        });
    }

    remove(i) {
        this.products.splice(i, 1);
    }

    onSearch() {
        this.searchServiceDialog.open();
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubcriptions);
    }
}
