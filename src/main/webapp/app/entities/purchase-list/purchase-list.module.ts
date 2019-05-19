import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    PurchaseListComponent,
    PurchaseListDetailComponent,
    PurchaseListUpdateComponent,
    PurchaseListDeletePopupComponent,
    PurchaseListDeleteDialogComponent,
    purchaseListRoute,
    purchaseListPopupRoute
} from './';

const ENTITY_STATES = [...purchaseListRoute, ...purchaseListPopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PurchaseListComponent,
        PurchaseListDetailComponent,
        PurchaseListUpdateComponent,
        PurchaseListDeleteDialogComponent,
        PurchaseListDeletePopupComponent
    ],
    entryComponents: [
        PurchaseListComponent,
        PurchaseListUpdateComponent,
        PurchaseListDeleteDialogComponent,
        PurchaseListDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuPurchaseListModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
