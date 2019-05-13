import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    ReturnItemComponent,
    ReturnItemDetailComponent,
    ReturnItemUpdateComponent,
    ReturnItemDeletePopupComponent,
    ReturnItemDeleteDialogComponent,
    returnItemRoute,
    returnItemPopupRoute
} from './';

const ENTITY_STATES = [...returnItemRoute, ...returnItemPopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReturnItemComponent,
        ReturnItemDetailComponent,
        ReturnItemUpdateComponent,
        ReturnItemDeleteDialogComponent,
        ReturnItemDeletePopupComponent
    ],
    entryComponents: [ReturnItemComponent, ReturnItemUpdateComponent, ReturnItemDeleteDialogComponent, ReturnItemDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuReturnItemModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}