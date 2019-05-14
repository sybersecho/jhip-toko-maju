import { Component, OnInit, OnDestroy, Input, ElementRef, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { IReturnItem, ReturnItem, ProductStatus } from 'app/shared/model/return-item.model';
import { IProduct } from 'app/shared/model/product.model';
import { IReturnTransaction } from 'app/shared/model/return-transaction.model';
import { ProductService } from 'app/entities/product';
import { filter, map } from 'rxjs/operators';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-select-product-box',
    templateUrl: './select-product-box.component.html',
    styles: []
})
export class SelectProductBoxComponent implements OnInit, OnDestroy {
    eventSubscription: Subscription;
    returnItem: IReturnItem;
    aProductSelected = false;
    // tslint:disable-next-line: no-input-rename
    @Input('returnT') returnToko: IReturnTransaction;

    @ViewChild('barcode') barcodeField: ElementRef;
    @ViewChild('quantity') quantityField: ElementRef;

    constructor(
        private productService: ProductService,
        protected eventManager: JhiEventManager,
        protected jhiAlertService: JhiAlertService
    ) {
        this.returnItem = new ReturnItem();
    }

    ngOnInit() {
        this.registerEvent();
        this.barcodeField.nativeElement.focus();
    }

    ngOnDestroy(): void {
        this.eventManager.destroy(this.eventSubscription);
    }

    addItem(form: NgForm) {
        this.returnItem.createItem();
        this.returnToko.addItem(this.returnItem);
        this.returnToko.calculateTotalReturn();
        console.log('returned, ', this.returnToko);
        this.resetForm();
    }

    searchProduct() {
        if (!this.returnItem.barcode) {
            return;
        }
        this.productService
            .findByBarcode(this.returnItem.barcode)
            .pipe(
                filter((mayBeOk: HttpResponse<IProduct>) => mayBeOk.ok),
                map((response: HttpResponse<IProduct>) => response.body)
            ) // res[0] index to 0 because response from back end is array list of product
            .subscribe(
                (res: IProduct) => this.addSelectedItem(res[0]),
                (err: HttpErrorResponse) => {
                    console.error(err.message);
                    this.onError('error.somethingwrong');
                }
            );
    }

    resetForm() {
        this.returnItem = new ReturnItem();
        this.aProductSelected = false;
        this.barcodeField.nativeElement.focus();
        this.quantityField.nativeElement.value = 1;
    }

    protected registerEvent() {
        // this.eventSubscription = this.eventManager.subscribe('onSelectCustomerEvent', response => this.loadCustomerProduct());
        this.eventSubscription = this.eventManager.subscribe('onSelectProductEvent', response => {
            // this.foundProduct(response.data);
            this.addSelectedItem(response.data);
            console.log('data, ', response.data);
        });
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    protected addSelectedItem(product: IProduct): void {
        if (!product) {
            return;
        }
        this.aProductSelected = true;
        this.createReturnItem(product);
        this.quantityField.nativeElement.focus();
    }

    protected createReturnItem(product: IProduct): void {
        this.returnItem = new ReturnItem(
            null,
            product.barcode,
            product.name,
            1,
            product.sellingPrice,
            ProductStatus.GOOD,
            product.unitName,
            0,
            product.barcode,
            product.id,
            null
        );
    }
}
