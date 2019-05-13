/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { ReturnItemUpdateComponent } from 'app/entities/return-item/return-item-update.component';
import { ReturnItemService } from 'app/entities/return-item/return-item.service';
import { ReturnItem } from 'app/shared/model/return-item.model';

describe('Component Tests', () => {
    describe('ReturnItem Management Update Component', () => {
        let comp: ReturnItemUpdateComponent;
        let fixture: ComponentFixture<ReturnItemUpdateComponent>;
        let service: ReturnItemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [ReturnItemUpdateComponent]
            })
                .overrideTemplate(ReturnItemUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReturnItemUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReturnItemService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new ReturnItem(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.returnItem = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new ReturnItem();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.returnItem = entity;
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
