import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, NavigationError, NavigationStart } from '@angular/router';

import { JhiLanguageHelper } from 'app/core';
import { LoadingService } from 'app/shared';
import { Subscription } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html'
})
export class JhiMainComponent implements OnInit, OnDestroy {
    loading = false;
    loadingSubscription: Subscription;

    constructor(private jhiLanguageHelper: JhiLanguageHelper, private router: Router, private loadingService: LoadingService) {}

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'jhiptokomajuApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    ngOnInit() {
        let isEvent = false;
        this.router.events.subscribe(event => {
            if (event instanceof NavigationStart) {
                // this.loading = true;
                isEvent = true;
                this.showLoading();
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }

            if (event instanceof NavigationEnd) {
                // this.loading = false;
                isEvent = true;
                this.showLoading();
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }

            if (event instanceof NavigationError && event.error.status === 404) {
                // this.loading = false;
                isEvent = true;
                this.showLoading();
                this.router.navigate(['/404']);
            }
        });

        if (!isEvent) {
            this.showLoading();
        }
    }

    private showLoading() {
        this.loadingSubscription = this.loadingService.loadingStatus
            .pipe(debounceTime(200))
            .subscribe((value: boolean) => (this.loading = value));
    }

    ngOnDestroy(): void {
        this.loadingSubscription.unsubscribe();
    }
}
