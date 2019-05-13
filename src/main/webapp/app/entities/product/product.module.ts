import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JhiptokomajuSharedModule } from 'app/shared';
import {
    ProductComponent,
    ProductDetailComponent,
    ProductUpdateComponent,
    ProductDeletePopupComponent,
    ProductDeleteDialogComponent,
    productRoute,
    productPopupRoute,
    SearchProductComponent,
    ViewBarcodeComponent,
    ExtractProductComponent,
    SearchExtProductComponent,
    ImportProductComponent
} from './';

const ENTITY_STATES = [...productRoute, ...productPopupRoute];

@NgModule({
    imports: [JhiptokomajuSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProductComponent,
        ProductDetailComponent,
        ProductUpdateComponent,
        ProductDeleteDialogComponent,
        ProductDeletePopupComponent,
        SearchProductComponent,
        ViewBarcodeComponent,
        ExtractProductComponent,
        SearchExtProductComponent,
        ImportProductComponent
    ],
    entryComponents: [
        ProductComponent,
        ProductUpdateComponent,
        ProductDeleteDialogComponent,
        ProductDeletePopupComponent,
        ViewBarcodeComponent,
        ExtractProductComponent,
        SearchExtProductComponent,
        ImportProductComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    exports: [SearchProductComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuProductModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
