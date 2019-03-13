export interface IProjectProduct {
    id?: number;
    specialPrice?: number;
    productName?: string;
    productId?: number;
    projectName?: string;
    projectId?: number;
}

export class ProjectProduct implements IProjectProduct {
    constructor(
        public id?: number,
        public specialPrice?: number,
        public productName?: string,
        public productId?: number,
        public projectName?: string,
        public projectId?: number
    ) {}
}
