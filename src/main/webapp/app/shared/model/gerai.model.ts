import { Moment } from 'moment';

export interface IGerai {
    id?: number;
    code?: string;
    name?: string;
    location?: string;
    password?: string;
    createdDate?: Moment;
    creatorLogin?: string;
    creatorId?: number;
}

export class Gerai implements IGerai {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public location?: string,
        public password?: string,
        public createdDate?: Moment,
        public creatorLogin?: string,
        public creatorId?: number
    ) {}
}
