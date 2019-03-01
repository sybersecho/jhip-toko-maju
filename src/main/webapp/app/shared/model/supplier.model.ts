export interface ISupplier {
    id?: number;
    name?: string;
    code?: string;
    address?: string;
    noTelp?: string;
    bankAccount?: string;
    bankName?: string;
}

export class Supplier implements ISupplier {
    constructor(
        public id?: number,
        public name?: string,
        public code?: string,
        public address?: string,
        public noTelp?: string,
        public bankAccount?: string,
        public bankName?: string
    ) {}
}
