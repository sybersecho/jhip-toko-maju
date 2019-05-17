import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import { ProductStockComponent, productStockRoute, ProductSearchStockDialogComponent } from './';

const ENTITY_STATES = [...productStockRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ProductStockComponent, ProductSearchStockDialogComponent],
    entryComponents: [ProductStockComponent, ProductSearchStockDialogComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuProductStockModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
