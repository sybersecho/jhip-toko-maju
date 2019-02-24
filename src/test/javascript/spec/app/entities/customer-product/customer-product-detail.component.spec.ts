/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { CustomerProduct } from 'app/shared/model/customer-product.model';
import { CustomerProductDetailComponent } from 'app/entities/customer/customer-product';

describe('Component Tests', () => {
    describe('CustomerProduct Management Detail Component', () => {
        let comp: CustomerProductDetailComponent;
        let fixture: ComponentFixture<CustomerProductDetailComponent>;
        const route = ({ data: of({ customerProduct: new CustomerProduct(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [CustomerProductDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CustomerProductDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CustomerProductDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.customerProduct).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
