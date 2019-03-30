import { Component, OnInit, OnDestroy } from '@angular/core';
import { IProduct, Product } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { ISaleItem, SaleItem } from 'app/shared/model/sale-item.model';
import { NgForm } from '@angular/forms';
import { resource } from 'selenium-webdriver/http';
import { isString } from '@ng-bootstrap/ng-bootstrap/util/util';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-product-box',
    templateUrl: './product-box.component.html',
    styles: []
})
export class ProductBoxComponent implements OnInit, OnDestroy {
    products: IProduct[];
    selectedProduct: IProduct;
    selectedItem: ISaleItem = new SaleItem();
    saleSavedEventSub: Subscription;
    addProductEventSub: Subscription;
    searchBarcode: string;

    constructor(
        private productService: ProductService,
        protected eventManager: JhiEventManager,
        protected jhiAlertService: JhiAlertService
    ) {
        this.selectedItem.quantity = 1;
    }

    ngOnInit() {
        // this.loadProducts();
        this.registerSaleSavedEvent();
        this.registerAddSelectProductEvent();
    }

    protected registerAddSelectProductEvent(): any {
        this.addProductEventSub = this.eventManager.subscribe('onSelectProductEvent', response => {
            this.selectedItem.setProduct(response.data);
        });
    }

    searchProduct() {
        this.productService
            .findByBarcode(this.searchBarcode)
            .pipe(
                filter((mayBeOk: HttpResponse<IProduct>) => mayBeOk.ok),
                map((response: HttpResponse<IProduct>) => response.body)
            ) // res[0] index to 0 because response from back end is array list of product
            .subscribe((res: IProduct) => this.foundProduct(res[0]), (err: HttpErrorResponse) => this.onError(err.message));
    }

    clearSelectedProduct() {
        this.selectedItem.setProduct(new Product());
    }

    protected foundProduct(found: IProduct): void {
        if (found) {
            this.selectedItem.setProduct(found);
        }
    }

    protected loadProducts(): void {
        this.productService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IProduct[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProduct[]>) => response.body)
            )
            .subscribe((res: IProduct[]) => (this.products = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    protected registerSaleSavedEvent(): void {
        this.saleSavedEventSub = this.eventManager.subscribe('saleSavedEvent', response => this.loadProducts());
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.saleSavedEventSub);
        this.eventManager.destroy(this.addProductEventSub);
    }

    checkStock(): boolean {
        return this.selectedItem.isQtyBigerThanStock();
    }

    isProductSelected(): boolean {
        return this.selectedItem.product === null || this.selectedItem.product === undefined || this.selectedItem.product.id === undefined;
    }

    addToCart(form: NgForm) {
        this.eventManager.broadcast({ name: 'addItemEvent', item: this.selectedItem.createItem() });
        this.reset();
        form.resetForm(this.selectedItem);
    }

    protected reset(): any {
        this.selectedItem = new SaleItem();
        this.selectedItem.quantity = 1;
    }

    trackProductById(index: number, item: IProduct) {
        return item.id;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
