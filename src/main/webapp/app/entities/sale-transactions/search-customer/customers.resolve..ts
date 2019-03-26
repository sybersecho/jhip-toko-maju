import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class CustomersResolve implements Resolve<ICustomer[]> {
    constructor(private service: CustomerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICustomer[]> {
        return this.service.query().pipe(
            filter((response: HttpResponse<Customer[]>) => response.ok),
            map((customer: HttpResponse<Customer[]>) => customer.body)
        );
    }
}
