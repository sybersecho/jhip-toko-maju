/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { SaleItemUpdateComponent } from 'app/entities/sale-item/sale-item-update.component';
import { SaleItemService } from 'app/entities/sale-item/sale-item.service';
import { SaleItem } from 'app/shared/model/sale-item.model';

describe('Component Tests', () => {
    describe('SaleItem Management Update Component', () => {
        let comp: SaleItemUpdateComponent;
        let fixture: ComponentFixture<SaleItemUpdateComponent>;
        let service: SaleItemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [SaleItemUpdateComponent]
            })
                .overrideTemplate(SaleItemUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SaleItemUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleItemService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SaleItem(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.saleItem = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SaleItem();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.saleItem = entity;
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
