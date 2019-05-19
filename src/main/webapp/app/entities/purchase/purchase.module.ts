import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    PurchaseComponent,
    PurchaseDetailComponent,
    PurchaseUpdateComponent,
    PurchaseDeletePopupComponent,
    PurchaseDeleteDialogComponent,
    purchaseRoute,
    purchasePopupRoute
} from './';
import { SearchSupplierProductComponent } from './search-supplier-product/search-supplier-product.component';

const ENTITY_STATES = [...purchaseRoute, ...purchasePopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PurchaseComponent,
        PurchaseDetailComponent,
        PurchaseUpdateComponent,
        PurchaseDeleteDialogComponent,
        PurchaseDeletePopupComponent,
        SearchSupplierProductComponent
    ],
    entryComponents: [
        PurchaseComponent,
        PurchaseUpdateComponent,
        PurchaseDeleteDialogComponent,
        PurchaseDeletePopupComponent,
        SearchSupplierProductComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuPurchaseModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
