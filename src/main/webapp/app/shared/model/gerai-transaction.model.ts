import { Moment } from 'moment';

export interface IGeraiTransaction {
    id?: number;
    barcode?: string;
    name?: string;
    quantity?: number;
    currentStock?: number;
    geraiId?: number;
    receivedDate?: Moment;
}

export class GeraiTransaction {
    constructor(
        public id?: number,
        public barcode?: string,
        public name?: string,
        public quantity?: number,
        public currentStock?: number,
        public geraiId?: number,
        public receivedDate?: Moment
    ) {}
}
