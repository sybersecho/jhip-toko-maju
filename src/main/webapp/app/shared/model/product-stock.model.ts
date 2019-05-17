export interface IProductStock {
    id?: number;
}

export class ProductStock implements IProductStock {
    constructor(public id?: number) {}
}
