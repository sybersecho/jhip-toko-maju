import { IProject } from 'app/shared/model/project.model';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';

export interface ICustomer {
    id?: number;
    code?: string;
    name?: string;
    address?: string;
    noTelp?: string;
    projects?: IProject[];
    products?: ICustomerProduct[];
}

export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public address?: string,
        public noTelp?: string,
        public projects?: IProject[],
        public products?: ICustomerProduct[]
    ) {}
}
