import { ICustomer } from 'app/shared/model/customer.model';

export interface IProject {
    id?: number;
    name?: string;
    address?: string;
    customer?: ICustomer;
}

export class Project implements IProject {
    constructor(public id?: number, public name?: string, public address?: string, public customer?: ICustomer) {}
}
