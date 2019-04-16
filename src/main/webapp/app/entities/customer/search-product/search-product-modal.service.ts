import { Injectable, Component } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SearchProductComponent } from './search-product.component';

@Injectable({
    providedIn: 'root'
})
export class SearchProductModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    open(): NgbModalRef {
        console.log('search-product open');
        console.log(this.isOpen);
        if (this.isOpen) {
            return;
        }

        this.isOpen = true;
        const modalRef = this.modalService.open(SearchProductComponent as Component, { size: 'lg', backdrop: 'static' });
        modalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return modalRef;
        // }, 100);
    }
}
