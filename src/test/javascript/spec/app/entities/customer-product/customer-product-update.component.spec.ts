/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { CustomerProduct } from 'app/shared/model/customer-product.model';
import { CustomerProductUpdateComponent, CustomerProductService } from 'app/entities/customer/customer-product';

describe('Component Tests', () => {
    describe('CustomerProduct Management Update Component', () => {
        let comp: CustomerProductUpdateComponent;
        let fixture: ComponentFixture<CustomerProductUpdateComponent>;
        let service: CustomerProductService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [CustomerProductUpdateComponent]
            })
                .overrideTemplate(CustomerProductUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CustomerProductUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CustomerProductService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CustomerProduct(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.customerProduct = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CustomerProduct();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.customerProduct = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
