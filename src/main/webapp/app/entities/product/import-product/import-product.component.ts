import { Component, OnInit } from '@angular/core';
import * as XLSX from 'xlsx';
import { ProductService } from '../product.service';
import { IProduct } from 'app/shared/model/product.model';

@Component({
    selector: 'jhi-import-product',
    templateUrl: './import-product.component.html',
    styles: []
})
export class ImportProductComponent implements OnInit {
    fileDir: String;
    arrayBuffer: any;
    file: File;
    arrayLoad: any[];

    constructor(protected productService: ProductService) {
        this.arrayLoad = [];
    }

    ngOnInit() {}

    incomingfile(event) {
        this.file = event.target.files[0];
    }

    importFile() {
        this.arrayLoad = [];
        const fileReader = new FileReader();
        fileReader.onload = e => {
            this.arrayBuffer = fileReader.result;
            const data = new Uint8Array(this.arrayBuffer);
            const arr = new Array();
            for (let i = 0; i !== data.length; ++i) {
                arr[i] = String.fromCharCode(data[i]);
            }
            const bstr = arr.join('');
            const workbook = XLSX.read(bstr, { type: 'binary' });
            const first_sheet_name = workbook.SheetNames[0];
            const worksheet = workbook.Sheets[first_sheet_name];
            this.arrayLoad = XLSX.utils.sheet_to_json(worksheet, { raw: true });
            console.log(this.arrayLoad);
        };
        fileReader.readAsArrayBuffer(this.file);
    }

    save() {
        this.productService.importProduct(this.arrayLoad).subscribe(res => this.onSuccess(res.body), error => this.onError(error.message));
    }

    onSuccess(products: IProduct[]): void {
        console.log('saved prod, ', products);
        this.arrayBuffer = [];
        this.arrayLoad = [];
    }

    onError(message: any): void {
        console.error(message);
    }
}
