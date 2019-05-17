export interface ISupplierStock {
    id?: number;
}

export class SupplierStock implements ISupplierStock {
    constructor(public id?: number) {}
}
