/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhiptokomajuTestModule } from '../../../test.module';
import { CustomerProduct } from 'app/shared/model/customer-product.model';
import { CustomerProductComponent, CustomerProductService } from 'app/entities/customer/customer-product';

describe('Component Tests', () => {
    describe('CustomerProduct Management Component', () => {
        let comp: CustomerProductComponent;
        let fixture: ComponentFixture<CustomerProductComponent>;
        let service: CustomerProductService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [CustomerProductComponent],
                providers: []
            })
                .overrideTemplate(CustomerProductComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CustomerProductComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CustomerProductService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new CustomerProduct(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.customerProducts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
