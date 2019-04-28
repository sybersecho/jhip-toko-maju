import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { JhiptokomajuSharedModule } from 'app/shared';
// import {  } from './info-product/info-product.component';
import {
    CustomerComponent,
    CustomerDetailComponent,
    CustomerUpdateComponent,
    CustomerDeleteDialogComponent,
    CustomerDeletePopupComponent,
    CustomerProductComponent,
    InfoProductComponent,
    customerRoute,
    customerPopupRoute,
    CustomerInvoiceComponent,
    SearchProductComponent,
    CustomerProjectComponent,
    InvoiceDetailDialogComponent
} from './';
// import { InvoiceDetailDialogComponent } from './customer-invoice/invoice-detail-dialog/invoice-detail-dialog.component';
// import { InvoiceDetailComponent } from './customer-invoice/invoice-detail/invoice-detail.component';
// import { CustomerProjectComponent } from './customer-project/customer-project.component';

const ENTITY_STATES = [...customerRoute, ...customerPopupRoute];

@NgModule({
    imports: [RouterModule.forChild(ENTITY_STATES), JhiptokomajuSharedModule],
    declarations: [
        CustomerComponent,
        CustomerDetailComponent,
        CustomerUpdateComponent,
        CustomerDeleteDialogComponent,
        CustomerDeletePopupComponent,
        InfoProductComponent,
        CustomerProductComponent,
        CustomerInvoiceComponent,
        SearchProductComponent,
        CustomerProjectComponent,
        InvoiceDetailDialogComponent
    ],
    entryComponents: [
        CustomerComponent,
        CustomerUpdateComponent,
        CustomerDeleteDialogComponent,
        CustomerDeletePopupComponent,
        InfoProductComponent,
        CustomerProductComponent,
        CustomerInvoiceComponent,
        SearchProductComponent,
        CustomerProjectComponent,
        InvoiceDetailDialogComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    exports: [RouterModule],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuCustomerModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
