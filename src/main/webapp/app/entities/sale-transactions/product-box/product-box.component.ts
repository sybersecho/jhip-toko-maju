import { Component, OnInit } from '@angular/core';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { ISaleItem, SaleItem } from 'app/shared/model/sale-item.model';
import { NgForm } from '@angular/forms';

@Component({
    selector: 'jhi-product-box',
    templateUrl: './product-box.component.html',
    styles: []
})
export class ProductBoxComponent implements OnInit {
    products: IProduct[];
    selectedProduct: IProduct;
    selectedItem: ISaleItem = new SaleItem();

    constructor(
        private productService: ProductService,
        protected eventManager: JhiEventManager,
        protected jhiAlertService: JhiAlertService
    ) {
        this.selectedItem.quantity = 1;
    }

    ngOnInit() {
        this.productService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IProduct[]>) => mayBeOk.ok),
                map((response: HttpResponse<IProduct[]>) => response.body)
            )
            .subscribe((res: IProduct[]) => (this.products = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    addToCart(form: NgForm) {
        this.calculateTotalPrice();
        this.eventManager.broadcast({ name: 'addItemEvent', item: this.selectedItem });
        this.reset();
        form.resetForm(this.selectedItem);
    }

    protected calculateTotalPrice(): any {
        this.selectedItem.totalPrice = this.selectedItem.product.sellingPrice * this.selectedItem.quantity;
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
