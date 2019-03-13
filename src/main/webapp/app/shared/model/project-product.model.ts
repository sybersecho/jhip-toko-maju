export interface IProjectProduct {
    id?: number;
    specialPrice?: number;
    productName?: string;
    productId?: number;
    projectName?: string;
    projectId?: number;
    barcode?: String;
    unitPrice?: number;
    sellingPrice?: number;
    unit?: String;
}

export class ProjectProduct implements IProjectProduct {
    constructor(
        public id?: number,
        public specialPrice?: number,
        public productName?: string,
        public productId?: number,
        public projectName?: string,
        public projectId?: number,
        public barcode?: String,
        public unitPrice?: number,
        public sellingPrice?: number,
        public unit?: String
    ) {}
}
