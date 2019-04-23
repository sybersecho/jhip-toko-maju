import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    DuePaymentComponent,
    DuePaymentDetailComponent,
    DuePaymentUpdateComponent,
    DuePaymentDeletePopupComponent,
    DuePaymentDeleteDialogComponent,
    duePaymentRoute,
    duePaymentPopupRoute
} from './';

const ENTITY_STATES = [...duePaymentRoute, ...duePaymentPopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DuePaymentComponent,
        DuePaymentDetailComponent,
        DuePaymentUpdateComponent,
        DuePaymentDeleteDialogComponent,
        DuePaymentDeletePopupComponent
    ],
    entryComponents: [DuePaymentComponent, DuePaymentUpdateComponent, DuePaymentDeleteDialogComponent, DuePaymentDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuDuePaymentModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
