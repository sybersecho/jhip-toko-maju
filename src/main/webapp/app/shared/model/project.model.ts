import { IProjectProduct } from 'app/shared/model/project-product.model';

export interface IProject {
    id?: number;
    name?: string;
    address?: string;
    code?: string;
    products?: IProjectProduct[];
    customerFirstName?: string;
    customerLastName?: string;
    customerFullName?: string;
    customerId?: number;
}

export class Project implements IProject {
    constructor(
        public id?: number,
        public name?: string,
        public address?: string,
        public code?: string,
        public products?: IProjectProduct[],
        public customerFirstName?: string,
        public customerLastName?: string,
        public customerFullName?: string,
        public customerId?: number
    ) {}
}
