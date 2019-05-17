/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { BadStockProductDetailComponent } from 'app/entities/bad-stock-product/bad-stock-product-detail.component';
import { BadStockProduct } from 'app/shared/model/bad-stock-product.model';

describe('Component Tests', () => {
    describe('BadStockProduct Management Detail Component', () => {
        let comp: BadStockProductDetailComponent;
        let fixture: ComponentFixture<BadStockProductDetailComponent>;
        const route = ({ data: of({ badStockProduct: new BadStockProduct(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [BadStockProductDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BadStockProductDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BadStockProductDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.badStockProduct).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
