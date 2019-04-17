import { Component, OnInit, Input } from '@angular/core';
import { ICustomer } from 'app/shared/model/customer.model';

@Component({
    selector: 'jhi-customer-project',
    templateUrl: './customer-project.component.html',
    styles: []
})
export class CustomerProjectComponent implements OnInit {
    @Input() customer: ICustomer;

    constructor() {}

    ngOnInit() {
        // console.log(this.customer);
    }
}
