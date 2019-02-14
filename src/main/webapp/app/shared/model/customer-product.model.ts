import { IProduct } from 'app/shared/model/product.model';
import { ICustomer } from 'app/shared/model/customer.model';

export interface ICustomerProduct {
    id?: number;
    specialPrice?: number;
    customerProducts?: IProduct[];
    customer?: ICustomer;
}

export class CustomerProduct implements ICustomerProduct {
    constructor(public id?: number, public specialPrice?: number, public customerProducts?: IProduct[], public customer?: ICustomer) {}
}
