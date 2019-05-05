import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { JhiptokomajuSharedLibsModule, JhiptokomajuSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { NgxCurrencyModule } from 'ngx-currency';
import { NgxBarcodeModule } from 'ngx-barcode';
import {} from '../layouts/print/print-layout.component';

@NgModule({
    imports: [JhiptokomajuSharedLibsModule, JhiptokomajuSharedCommonModule, NgxCurrencyModule, NgxBarcodeModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [JhiptokomajuSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, NgxCurrencyModule, NgxBarcodeModule],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhiptokomajuSharedModule {
    static forRoot() {
        return {
            ngModule: JhiptokomajuSharedModule
        };
    }
}
