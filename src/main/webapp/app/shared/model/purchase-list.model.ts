export interface IPurchaseList {
    id?: number;
    barcode?: string;
    name?: string;
    unit?: string;
    unitPrice?: number;
    quantity?: number;
    total?: number;
    purchaseId?: number;

    calculateTotal();
}

export class PurchaseList implements IPurchaseList {
    constructor(
        public id?: number,
        public barcode?: string,
        public name?: string,
        public unit?: string,
        public unitPrice?: number,
        public quantity?: number,
        public total?: number,
        public purchaseId?: number
    ) {
        this.total = 0;
    }

    calculateTotal() {
        this.total = this.quantity * this.unitPrice;
    }
}
