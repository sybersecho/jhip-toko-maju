import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    CustomerProductComponent,
    CustomerProductDetailComponent,
    CustomerProductUpdateComponent,
    CustomerProductDeletePopupComponent,
    CustomerProductDeleteDialogComponent,
    customerProductRoute,
    customerProductPopupRoute
} from './';

const ENTITY_STATES = [...customerProductRoute, ...customerProductPopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CustomerProductComponent,
        CustomerProductDetailComponent,
        CustomerProductUpdateComponent,
        CustomerProductDeleteDialogComponent,
        CustomerProductDeletePopupComponent
    ],
    entryComponents: [
        CustomerProductComponent,
        CustomerProductUpdateComponent,
        CustomerProductDeleteDialogComponent,
        CustomerProductDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuCustomerProductModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
