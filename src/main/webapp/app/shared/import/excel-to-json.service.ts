import { Injectable } from '@angular/core';
import * as XLSX from 'xlsx';

@Injectable({
    providedIn: 'root'
})
export class ExcelToJsonService {
    arrayLoad: any[];
    arrayBuffer: any;
    file: File;

    constructor() {}

    importFile(file: File): any {
        console.log('file, ', file);
        this.arrayLoad = [];
        const fileReader = new FileReader();
        fileReader.onload = e => {
            this.arrayBuffer = fileReader.result;
            const data = new Uint8Array(this.arrayBuffer);
            const arr = new Array();
            for (let i = 0; i !== data.length; ++i) {
                arr[i] = String.fromCharCode(data[i]);
            }
            const bstr = arr.join('');
            const workbook = XLSX.read(bstr, { type: 'binary' });
            const first_sheet_name = workbook.SheetNames[0];
            const worksheet = workbook.Sheets[first_sheet_name];
            this.arrayLoad = XLSX.utils.sheet_to_json(worksheet, { raw: true });
            console.log(this.arrayLoad);
        };
        fileReader.readAsArrayBuffer(this.file);
        return this.arrayLoad;
    }
}
