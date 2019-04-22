import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { ICustomerProduct } from 'app/shared/model/customer-product.model';
import { Observable } from 'rxjs';
import { CustomerService } from '../customer.service';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class CustomerProductResolve implements Resolve<ICustomerProduct[]> {
    private products: ICustomerProduct[];
    constructor(private service: CustomerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): ICustomerProduct[] | Observable<ICustomerProduct[]> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.searcyByCustomer(id).pipe(
                filter((response: HttpResponse<ICustomerProduct[]>) => response.ok),
                map((customer: HttpResponse<ICustomerProduct[]>) => customer.body)
            );
        }
        return this.products;
    }
}
