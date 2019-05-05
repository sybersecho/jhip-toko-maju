import { Routes } from '@angular/router';
import { PrintLayoutComponent } from './print-layout.component';
import { PrintBillLayoutComponent } from './print-bill-layout/print-bill-layout.component';

export const printRoute: Routes = [
    {
        path: 'printt',
        component: PrintLayoutComponent,
        outlet: 'print',
        children: [{ path: 'bill', component: PrintBillLayoutComponent }]
    }
];
