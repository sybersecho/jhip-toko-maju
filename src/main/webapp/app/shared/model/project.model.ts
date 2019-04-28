import { Moment } from 'moment';
import { IProjectProduct } from 'app/shared/model/project-product.model';

export interface IProject {
    id?: number;
    name?: string;
    address?: string;
    code?: string;
    city?: string;
    province?: string;
    postalCode?: string;
    createdDate?: Moment;
    modifiedDate?: Moment;
    products?: IProjectProduct[];
    customerFirstName?: string;
    customerId?: number;
    creatorLogin?: string;
    creatorId?: number;
    changerLogin?: string;
    changerId?: number;
    customerLastName?: string;
    customerFullName?: string;
}

export class Project implements IProject {
    constructor(
        public id?: number,
        public name?: string,
        public address?: string,
        public code?: string,
        public city?: string,
        public province?: string,
        public postalCode?: string,
        public createdDate?: Moment,
        public modifiedDate?: Moment,
        public products?: IProjectProduct[],
        public customerFirstName?: string,
        public customerId?: number,
        public creatorLogin?: string,
        public creatorId?: number,
        public changerLogin?: string,
        public changerId?: number,
        public customerLastName?: string,
        public customerFullName?: string
    ) {}
}
