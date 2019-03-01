import { IProduct } from 'app/shared/model/product.model';

export interface ISupplier {
    id?: number;
    name?: string;
    code?: string;
    address?: string;
    noTelp?: string;
    bankAccount?: string;
    bankName?: string;
    products?: IProduct[];
}

export class Supplier implements ISupplier {
    constructor(
        public id?: number,
        public name?: string,
        public code?: string,
        public address?: string,
        public noTelp?: string,
        public bankAccount?: string,
        public bankName?: string,
        public products?: IProduct[]
    ) {}
}
