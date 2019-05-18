import { Moment } from 'moment';
import { StockOrderRequest } from './stock-order-request.model';
import { IProduct } from './product.model';

export interface IStockOrder {
    id?: number;
    siteLocation?: string;
    createdDate?: Moment;
    processed?: boolean;
    processedDate?: Moment;
    creatorLogin?: string;
    creatorId?: number;
    approvalLogin?: string;
    approvalId?: number;
    totalOrder?: number;
    stockOrderRequests?: StockOrderRequest[];

    addStockRequest(selectedProduct: IProduct): Boolean;
    removeRequest(i: number);
    calculateTotalOrder();
    changeQuantity(i: number);
    isOrderEmpty(): Boolean;
}

export class StockOrder implements IStockOrder {
    constructor(
        public id?: number,
        public siteLocation?: string,
        public createdDate?: Moment,
        public processed?: boolean,
        public processedDate?: Moment,
        public creatorLogin?: string,
        public creatorId?: number,
        public approvalLogin?: string,
        public approvalId?: number,
        public totalOrder?: number,
        public stockOrderRequests?: StockOrderRequest[]
    ) {
        this.processed = this.processed || false;
        this.stockOrderRequests = [];
        this.totalOrder = 0;
    }

    addStockRequest(product: IProduct): Boolean {
        const isExist = this.exist(product.barcode);
        if (isExist) {
            return false;
        }

        const request = new StockOrderRequest(product.barcode, product.name, product.unitName, product.unitPrice, 1, product.unitPrice);

        this.stockOrderRequests.push(request);
        this.calculateTotalOrder();

        return true;
    }

    removeRequest(i: number) {
        this.stockOrderRequests.splice(i, 1);
        this.calculateTotalOrder();
    }

    isOrderEmpty(): Boolean {
        return this.stockOrderRequests && this.stockOrderRequests.length > 0;
    }

    changeQuantity(i: number) {
        const order = this.stockOrderRequests[i];
        if (order.quantity <= 0) {
            this.removeRequest(i);
        }
        this.calculateTotalOrder();
    }

    calculateTotalOrder() {
        this.totalOrder = 0;
        this.stockOrderRequests.forEach(it => {
            it.calculateTotal();
            this.totalOrder += it.totalPrice;
        });
    }

    protected exist(barcode: string): Boolean {
        return this.stockOrderRequests.findIndex(p => p.barcode === barcode) > -1;
    }
}
