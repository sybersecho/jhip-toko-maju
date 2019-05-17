/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { ProductStockDetailComponent } from 'app/entities/product-stock/product-stock-detail.component';
import { ProductStock } from 'app/shared/model/product-stock.model';

describe('Component Tests', () => {
    describe('ProductStock Management Detail Component', () => {
        let comp: ProductStockDetailComponent;
        let fixture: ComponentFixture<ProductStockDetailComponent>;
        const route = ({ data: of({ productStock: new ProductStock(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [ProductStockDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ProductStockDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProductStockDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.productStock).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
