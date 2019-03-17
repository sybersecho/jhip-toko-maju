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
        ProductBoxComponent
    ],
    entryComponents: [
        SaleTransactionsComponent,
        SaleTransactionsUpdateComponent,
        SaleTransactionsDeleteDialogComponent,
        SaleTransactionsDeletePopupComponent
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
