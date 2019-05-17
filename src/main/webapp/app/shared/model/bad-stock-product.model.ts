export interface IBadStockProduct {
    id?: number;
    barcode?: string;
    productName?: string;
    quantity?: number;
}

export class BadStockProduct implements IBadStockProduct {
    constructor(public id?: number, public barcode?: string, public productName?: string, public quantity?: number) {}
}
