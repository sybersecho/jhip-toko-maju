import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    CancelTransactionComponent,
    CancelTransactionDetailComponent,
    CancelTransactionUpdateComponent,
    CancelTransactionDeletePopupComponent,
    CancelTransactionDeleteDialogComponent,
    cancelTransactionRoute,
    cancelTransactionPopupRoute
} from './';

const ENTITY_STATES = [...cancelTransactionRoute, ...cancelTransactionPopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CancelTransactionComponent,
        CancelTransactionDetailComponent,
        CancelTransactionUpdateComponent,
        CancelTransactionDeleteDialogComponent,
        CancelTransactionDeletePopupComponent
    ],
    entryComponents: [
        CancelTransactionComponent,
        CancelTransactionUpdateComponent,
        CancelTransactionDeleteDialogComponent,
        CancelTransactionDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuCancelTransactionModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
