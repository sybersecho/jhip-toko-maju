import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    ReturnTransactionComponent,
    ReturnTransactionDetailComponent,
    ReturnTransactionUpdateComponent,
    ReturnTransactionDeletePopupComponent,
    ReturnTransactionDeleteDialogComponent,
    returnTransactionRoute,
    returnTransactionPopupRoute,
    ReturnTokoComponent,
    CustomerInfoBoxComponent,
    SelectProductBoxComponent
} from './';

const ENTITY_STATES = [...returnTransactionRoute, ...returnTransactionPopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReturnTransactionComponent,
        ReturnTransactionDetailComponent,
        ReturnTransactionUpdateComponent,
        ReturnTransactionDeleteDialogComponent,
        ReturnTransactionDeletePopupComponent,
        ReturnTokoComponent,
        CustomerInfoBoxComponent,
        SelectProductBoxComponent
    ],
    entryComponents: [
        ReturnTransactionComponent,
        ReturnTransactionUpdateComponent,
        ReturnTransactionDeleteDialogComponent,
        ReturnTransactionDeletePopupComponent,
        ReturnTokoComponent,
        CustomerInfoBoxComponent,
        SelectProductBoxComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuReturnTransactionModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
