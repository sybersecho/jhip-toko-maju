import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiptokomajuCustomerModule } from './customer/customer.module';
import { JhiptokomajuProductModule } from './product/product.module';
import { JhiptokomajuProjectModule } from './project/project.module';
import { JhiptokomajuSupplierModule } from './supplier/supplier.module';
import { JhiptokomajuSaleTransactionsModule } from './sale-transactions/sale-transactions.module';
import { JhiptokomajuInvoiceModule } from './invoice/invoice.module';
import { JhiptokomajuDuePaymentModule } from './due-payment/due-payment.module';
import { JhiptokomajuReturnTransactionModule } from './return-transaction/return-transaction.module';
import { JhiptokomajuProductStockModule } from './product-stock/product-stock.module';
import { JhiptokomajuSupplierStockModule } from './supplier-stock/supplier-stock.module';
import { JhiptokomajuPurchaseModule } from './purchase/purchase.module';

@NgModule({
    imports: [
        JhiptokomajuCustomerModule,
        JhiptokomajuProductModule,
        JhiptokomajuProjectModule,
        JhiptokomajuSupplierModule,
        JhiptokomajuSaleTransactionsModule,
        JhiptokomajuInvoiceModule,
        JhiptokomajuDuePaymentModule,
        JhiptokomajuReturnTransactionModule,
        JhiptokomajuProductStockModule,
        JhiptokomajuSupplierStockModule,
        JhiptokomajuPurchaseModule,
        RouterModule.forChild([
            {
                path: 'unit',
                loadChildren: './unit/unit.module#JhiptokomajuUnitModule'
            },
            {
                path: 'cancel-transaction',
                loadChildren: './cancel-transaction/cancel-transaction.module#JhiptokomajuCancelTransactionModule'
            },
            {
                path: 'sale-report',
                loadChildren: './sale-report/sale-report.module#JhiptokomajuSaleReportModule'
            },
            {
                path: 'stock-order',
                loadChildren: './stock-order/stock-order.module#JhiptokomajuStockOrderModule'
                // },
                // {
                //     path: 'stock-order',
                //     loadChildren: './stock-order/stock-order.module#JhiptokomajuStockOrderModule'
                // },
                // {
                //     path: 'stock-order-request',
                //     loadChildren: './stock-order-request/stock-order-request.module#JhiptokomajuStockOrderRequestModule'
                // },
                // {
                //     path: 'purchase',
                //     loadChildren: './purchase/purchase.module#JhiptokomajuPurchaseModule'
            },
            {
                path: 'purchase-list',
                loadChildren: './purchase-list/purchase-list.module#JhiptokomajuPurchaseListModule'
            },
            {
                path: 'report',
                loadChildren: './report/report.module#JhiptokomajuReportModule'
            },
            {
                path: 'gerai',
                loadChildren: './gerai/gerai.module#JhiptokomajuGeraiModule'
            },
            {
                path: 'gerai-config',
                loadChildren: './gerai-config/gerai-config.module#JhiptokomajuGeraiConfigModule'
            }
            //     /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuEntityModule {}
