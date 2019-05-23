import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    GeraiComponent,
    GeraiDetailComponent,
    GeraiUpdateComponent,
    GeraiDeletePopupComponent,
    GeraiDeleteDialogComponent,
    geraiRoute,
    geraiPopupRoute
} from './';

const ENTITY_STATES = [...geraiRoute, ...geraiPopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [GeraiComponent, GeraiDetailComponent, GeraiUpdateComponent, GeraiDeleteDialogComponent, GeraiDeletePopupComponent],
    entryComponents: [GeraiComponent, GeraiUpdateComponent, GeraiDeleteDialogComponent, GeraiDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuGeraiModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
