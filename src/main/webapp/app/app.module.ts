import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { NgJhipsterModule } from 'ng-jhipster';
import { NgxLoadingModule, ngxLoadingAnimationTypes } from 'ngx-loading';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { LoadingInterceptor } from './blocks/interceptor/loading-interceptor';
import { JhiptokomajuSharedModule } from 'app/shared';
import { JhiptokomajuCoreModule } from 'app/core';
import { JhiptokomajuAppRoutingModule } from './app-routing.module';
import { JhiptokomajuHomeModule } from './home/home.module';
import { JhiptokomajuAccountModule } from './account/account.module';
import { JhiptokomajuEntityModule } from './entities/entity.module';
import * as moment from 'moment';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent,
    PrintLayoutComponent,
    PrintBillLayoutComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        NgxWebstorageModule.forRoot({ prefix: 'jhi', separator: '-' }),
        NgxLoadingModule.forRoot({
            animationType: ngxLoadingAnimationTypes.rectangleBounce,
            backdropBackgroundColour: 'rgba(0,0,0,0.1)',
            backdropBorderRadius: '4px',
            primaryColour: '#ffffff',
            secondaryColour: '#ffffff',
            tertiaryColour: '#ffffff'
        }),
        NgJhipsterModule.forRoot({
            // set below to true to make alerts look like toast
            alertAsToast: false,
            alertTimeout: 5000,
            i18nEnabled: true,
            defaultI18nLang: 'id'
        }),
        JhiptokomajuSharedModule.forRoot(),
        JhiptokomajuCoreModule,
        JhiptokomajuHomeModule,
        JhiptokomajuAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        JhiptokomajuEntityModule,
        JhiptokomajuAppRoutingModule
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent,
        PrintLayoutComponent,
        PrintBillLayoutComponent
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: LoadingInterceptor,
            multi: true
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class JhiptokomajuAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
