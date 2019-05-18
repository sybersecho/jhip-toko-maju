import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import { StockOrderComponent, stockOrderRoute } from './';
import { StockOrderInputComponent } from './stock-order-input.component';
import { StockOrderProcessComponent } from './stock-order-process.component';

const ENTITY_STATES = [...stockOrderRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [StockOrderComponent, StockOrderInputComponent, StockOrderProcessComponent],
    entryComponents: [StockOrderComponent, StockOrderInputComponent, StockOrderProcessComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuStockOrderModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
