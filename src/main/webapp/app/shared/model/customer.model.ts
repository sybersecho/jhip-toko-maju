import { ICustomerProduct } from 'app/shared/model/customer-product.model';

export const enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE'
}

export interface ICustomer {
    id?: number;
    code?: string;
    firstName?: string;
    lastName?: string;
    gender?: Gender;
    phoneNumber?: string;
    address?: string;
    products?: ICustomerProduct[];
}

export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public code?: string,
        public firstName?: string,
        public lastName?: string,
        public gender?: Gender,
        public phoneNumber?: string,
        public address?: string,
        public products?: ICustomerProduct[]
    ) {}
}
