export interface IProduct {
    id?: number;
    barcode?: string;
    name?: string;
    warehousePrice?: number;
    unitPrice?: number;
    sellingPrice?: number;
    stock?: number;
    supplierName?: string;
    supplierId?: number;
    unitName?: string;
    unitId?: number;
    supplierCode?: string;
}

export class Product implements IProduct {
    constructor(
        public id?: number,
        public barcode?: string,
        public name?: string,
        public warehousePrice?: number,
        public unitPrice?: number,
        public sellingPrice?: number,
        public stock?: number,
        public supplierName?: string,
        public supplierId?: number,
        public unitName?: string,
        public unitId?: number,
        public supplierCode?: string
    ) {}
}
