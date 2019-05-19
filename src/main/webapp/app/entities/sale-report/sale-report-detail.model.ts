export class SaleReportDetail {
    customer?: string;
    total?: number;
    items?: ReportItem[];
}

export class ReportItem {
    barcode?: string;
    name?: string;
    unitName?: String;
    sellingPrice?: number;
    quantity?: number;
    totalPrice?: number;
}
