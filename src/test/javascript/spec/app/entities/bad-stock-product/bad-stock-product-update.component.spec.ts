/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { BadStockProductUpdateComponent } from 'app/entities/bad-stock-product/bad-stock-product-update.component';
import { BadStockProductService } from 'app/entities/bad-stock-product/bad-stock-product.service';
import { BadStockProduct } from 'app/shared/model/bad-stock-product.model';

describe('Component Tests', () => {
    describe('BadStockProduct Management Update Component', () => {
        let comp: BadStockProductUpdateComponent;
        let fixture: ComponentFixture<BadStockProductUpdateComponent>;
        let service: BadStockProductService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [BadStockProductUpdateComponent]
            })
                .overrideTemplate(BadStockProductUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BadStockProductUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BadStockProductService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new BadStockProduct(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.badStockProduct = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new BadStockProduct();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.badStockProduct = entity;
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
