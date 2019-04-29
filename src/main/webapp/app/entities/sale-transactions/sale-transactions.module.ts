import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    SaleTransactionsComponent,
    SaleTransactionsDetailComponent,
    SaleTransactionsUpdateComponent,
    SaleTransactionsDeletePopupComponent,
    SaleTransactionsDeleteDialogComponent,
    CustomerBoxComponent,
    saleTransactionsRoute,
    saleTransactionsPopupRoute,
    saleTransactionsPrintRoute
} from './';
import { MainCashierComponent } from './main-cashier.component';
import { ProductBoxComponent } from './product-box/product-box.component';
import {
    SaleTransactionsSearchDialogComponent,
    SaleTransactionsSearchPopupComponent
} from './search-customer/sale-transactions-search-dialog.component';
import { SearchProductDialogComponent, SearchProductPopupComponent } from './product-box/search-product-dialog.component';
import { PrintComponent } from './print/print.component';
import { DeliveryOrdersComponent } from './delivery-orders/delivery-orders.component';
// import { saleTransactionsPrintRoute } from './sale-transactions.route';

const ENTITY_STATES = [...saleTransactionsRoute, ...saleTransactionsPopupRoute, ...saleTransactionsPrintRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SaleTransactionsComponent,
        SaleTransactionsDetailComponent,
        SaleTransactionsUpdateComponent,
        SaleTransactionsDeleteDialogComponent,
        SaleTransactionsDeletePopupComponent,
        MainCashierComponent,
        ProductBoxComponent,
        SaleTransactionsSearchDialogComponent,
        SaleTransactionsSearchPopupComponent,
        SearchProductDialogComponent,
        SearchProductPopupComponent,
        PrintComponent,
        DeliveryOrdersComponent,
        CustomerBoxComponent
    ],
    entryComponents: [
        SaleTransactionsComponent,
        SaleTransactionsUpdateComponent,
        SaleTransactionsDeleteDialogComponent,
        SaleTransactionsDeletePopupComponent,
        MainCashierComponent,
        ProductBoxComponent,
        SaleTransactionsSearchDialogComponent,
        SaleTransactionsSearchPopupComponent,
        SearchProductDialogComponent,
        SearchProductPopupComponent,
        PrintComponent,
        DeliveryOrdersComponent,
        CustomerBoxComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuSaleTransactionsModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
