import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    SupplierComponent,
    SupplierDetailComponent,
    SupplierUpdateComponent,
    SupplierDeletePopupComponent,
    SupplierDeleteDialogComponent,
    SupplierSearchDialogComponent,
    supplierRoute,
    supplierPopupRoute
} from './';

const ENTITY_STATES = [...supplierRoute, ...supplierPopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SupplierComponent,
        SupplierDetailComponent,
        SupplierUpdateComponent,
        SupplierDeleteDialogComponent,
        SupplierDeletePopupComponent,
        SupplierSearchDialogComponent
    ],
    entryComponents: [
        SupplierComponent,
        SupplierUpdateComponent,
        SupplierDeleteDialogComponent,
        SupplierDeletePopupComponent,
        SupplierSearchDialogComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuSupplierModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
