/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { ProductStockUpdateComponent } from 'app/entities/product-stock/product-stock-update.component';
import { ProductStockService } from 'app/entities/product-stock/product-stock.service';
import { ProductStock } from 'app/shared/model/product-stock.model';

describe('Component Tests', () => {
    describe('ProductStock Management Update Component', () => {
        let comp: ProductStockUpdateComponent;
        let fixture: ComponentFixture<ProductStockUpdateComponent>;
        let service: ProductStockService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [ProductStockUpdateComponent]
            })
                .overrideTemplate(ProductStockUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ProductStockUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProductStockService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new ProductStock(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.productStock = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new ProductStock();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.productStock = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
