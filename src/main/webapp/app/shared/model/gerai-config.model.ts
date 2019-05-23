export interface IGeraiConfig {
    id?: number;
    codeGerai?: string;
    nameGerai?: string;
    password?: string;
    urlToko?: string;
    activated?: boolean;
}

export class GeraiConfig implements IGeraiConfig {
    constructor(
        public id?: number,
        public codeGerai?: string,
        public nameGerai?: string,
        public password?: string,
        public urlToko?: string,
        public activated?: boolean
    ) {
        this.activated = this.activated || false;
    }
}
