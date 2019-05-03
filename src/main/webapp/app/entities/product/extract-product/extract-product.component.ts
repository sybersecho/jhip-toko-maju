import { Component, OnInit } from '@angular/core';
import { ExtractProductModel } from './extract-product-model';
import { SearcExtProductDialogService } from './search-ext-product.component';

@Component({
    selector: 'jhi-extract-product',
    templateUrl: './extract-product.component.html',
    styles: []
})
export class ExtractProductComponent implements OnInit {
    products: ExtractProductModel[];

    constructor(protected searchServiceDialog: SearcExtProductDialogService) {
        this.products = [];
    }

    ngOnInit() {}

    onSearch() {
        this.searchServiceDialog.open();
    }
}
