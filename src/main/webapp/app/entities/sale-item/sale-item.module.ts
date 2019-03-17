import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    SaleItemComponent,
    SaleItemDetailComponent,
    SaleItemUpdateComponent,
    SaleItemDeletePopupComponent,
    SaleItemDeleteDialogComponent,
    saleItemRoute,
    saleItemPopupRoute
} from './';

const ENTITY_STATES = [...saleItemRoute, ...saleItemPopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SaleItemComponent,
        SaleItemDetailComponent,
        SaleItemUpdateComponent,
        SaleItemDeleteDialogComponent,
        SaleItemDeletePopupComponent
    ],
    entryComponents: [SaleItemComponent, SaleItemUpdateComponent, SaleItemDeleteDialogComponent, SaleItemDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuSaleItemModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
