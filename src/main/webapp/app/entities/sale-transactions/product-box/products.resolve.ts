import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { IProduct } from 'app/shared/model/product.model';
import { Observable } from 'rxjs';
import { ProductService } from 'app/entities/product';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class ProductsResolve implements Resolve<IProduct[]> {
    constructor(private service: ProductService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): IProduct[] | Observable<IProduct[]> | Promise<IProduct[]> {
        return this.service.query().pipe(
            filter((response: HttpResponse<IProduct[]>) => response.ok),
            map((product: HttpResponse<IProduct[]>) => product.body)
        );
    }
}
