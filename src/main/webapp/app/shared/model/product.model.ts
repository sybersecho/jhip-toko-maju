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
    warehousePrices?: number;
    unitPrices?: number;
    sellingPrices?: number;
    stock?: number;
}

export class Product implements IProduct {
    constructor(
        public id?: number,
        public barcode?: string,
        public name?: string,
        public unit?: UnitMeasure,
        public warehousePrices?: number,
        public unitPrices?: number,
        public sellingPrices?: number,
        public stock?: number
    ) {}
}
