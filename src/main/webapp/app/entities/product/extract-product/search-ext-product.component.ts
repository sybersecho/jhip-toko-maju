import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { IProduct, Product } from 'app/shared/model/product.model';
import { ProductService } from '../product.service';
import { NgbActiveModal, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SupplierService } from 'app/entities/supplier';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-search-ext-product',
    templateUrl: './search-ext-product.component.html',
    styles: []
})
export class SearchExtProductComponent implements OnInit {
    product: IProduct;
    bySupplier = false;

    constructor(
        public activeModal: NgbActiveModal,
        protected productService: ProductService,
        protected jhiAlertService: JhiAlertService,
        protected supplierService: SupplierService,
        protected eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.product = new Product();
    }

    addToList() {
        if (this.bySupplier) {
            this.addToListBySupplier();
        } else {
            this.addToListByProduct();
        }
    }

    protected addToListBySupplier() {
        if (!this.product.supplierCode) {
            this.onError('jhiptokomajuApp.product.extract.messages.supplier.empty');
            return;
        }

        this.productService.extractProductBySupplier(this.product.supplierCode).subscribe(
            res => {
                if (!res.body) {
                    this.onError('error.search.not.found');
                } else {
                    this.eventManager.broadcast({ name: 'onExtractProductBySupplierEvt', data: res.body });
                }
            },
            error => {
                console.log(error.message);
                this.onError('error.somethingwrong');
            }
        );
    }

    protected addToListByProduct() {
        if (!this.product.id) {
            this.onError('jhiptokomajuApp.product.extract.messages.productId.empty');
            return;
        }

        this.productService.extractProductById(this.product.id).subscribe(
            res => {
                this.eventManager.broadcast({ name: 'onEventProduct', data: res.body });
            },
            error => {
                console.log(error.message);
                this.onError('error.somethingwrong');
            }
        );
    }

    searchBarcode() {
        if (!this.product.barcode) {
            return;
        }
        this.productService.findByBarcode(this.product.barcode).subscribe(
            res => {
                console.log('body', res.body);
                if (!res.body[0]) {
                    this.onError('error.search.not.found');
                    this.resetByBarcode();
                } else {
                    this.product = res.body[0];
                }
            },
            error => {
                this.onError(error.message);
            }
        );
    }

    searchSupplier() {
        if (!this.product.supplierCode) {
            return;
        }

        this.productService.findBySupplierCode(this.product.supplierCode).subscribe(
            res => {
                console.log('body', res.body);

                if (!res.body[0]) {
                    this.onError('error.search.not.found');
                    this.resetBySupplier();
                } else {
                    this.product.supplierCode = res.body[0].supplierCode;
                    this.product.supplierName = res.body[0].supplierName;
                    this.product.supplierId = res.body[0].supplierId;
                }
            },
            error => {
                this.onError('error.somethingworng');
                this.resetBySupplier();
            }
        );
    }

    protected resetByBarcode() {
        this.product.id = null;
        this.product.name = '';
        this.product.sellingPrice = 0;
        this.product.stock = 0;
        this.product.unitPrice = 0;
        this.product.warehousePrice = 0;
        this.product.supplierCode = '';
    }

    protected resetBySupplier() {
        this.product.barcode = '';
        this.product.id = null;
        this.product.name = '';
        this.product.sellingPrice = 0;
        this.product.stock = 0;
        this.product.unitPrice = 0;
        this.product.warehousePrice = 0;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

@Injectable({
    providedIn: 'root'
})
export class SearcExtProductDialogService {
    private isOpen = false;
    protected ngbModalRef: NgbModalRef;

    constructor(private modalService: NgbModal) {}

    open() {
        if (this.isOpen) {
            return;
        }

        this.isOpen = true;
        this.ngbModalRef = this.modalService.open(SearchExtProductComponent as Component, { size: 'lg', backdrop: 'static' });

        this.ngbModalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return this.ngbModalRef;
    }
}
