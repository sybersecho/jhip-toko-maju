export const enum UnitMeasure {
    KG = 'KG',
    SAK = 'SAK',
    M = 'M'
}

export interface IProduct {
    id?: number;
    barcode?: string;
    name?: string;
    unit?: UnitMeasure;
    warehousePrice?: number;
    unitPrice?: number;
    sellingPrice?: number;
    stock?: number;
    supplierName?: string;
    supplierId?: number;
    supplierCode?: string;
}

export class Product implements IProduct {
    constructor(
        public id?: number,
        public barcode?: string,
        public name?: string,
        public unit?: UnitMeasure,
        public warehousePrice?: number,
        public unitPrice?: number,
        public sellingPrice?: number,
        public stock?: number,
        public supplierName?: string,
        public supplierId?: number,
        public supplierCode?: string
    ) {}
}
