export interface IReport {
    id?: number;
}

export class Report implements IReport {
    constructor(public id?: number) {}
}
