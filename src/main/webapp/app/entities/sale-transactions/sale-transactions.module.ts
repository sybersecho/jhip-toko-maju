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
    saleTransactionsRoute,
    saleTransactionsPopupRoute
} from './';
import { MainCashierComponent } from './main-cashier.component';
import { ProductBoxComponent } from './product-box/product-box.component';
import {
    SaleTransactionsSearchDialogComponent,
    SaleTransactionsSearchPopupComponent
} from './search-customer/sale-transactions-search-dialog.component';
import { SearchProductDialogComponent, SearchProductPopupComponent } from './product-box/search-product-dialog.component';

const ENTITY_STATES = [...saleTransactionsRoute, ...saleTransactionsPopupRoute];

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
        SearchProductPopupComponent
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
        SearchProductPopupComponent
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
