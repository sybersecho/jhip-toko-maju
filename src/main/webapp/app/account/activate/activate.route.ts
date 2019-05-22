import { Route } from '@angular/router';

import { ActivateComponent } from './activate.component';

export const activateRoute: Route = {
    path: 'activate',
    component: ActivateComponent,
    data: {
        authorities: ['ROLE_USER', 'ROLE_CASHIER', 'ROLE_ADMIN', 'ROLE_SUPERUSER'],
        pageTitle: 'activate.title'
    }
};
