/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { ReturnTransactionUpdateComponent } from 'app/entities/return-transaction/return-transaction-update.component';
import { ReturnTransactionService } from 'app/entities/return-transaction/return-transaction.service';
import { ReturnTransaction } from 'app/shared/model/return-transaction.model';

describe('Component Tests', () => {
    describe('ReturnTransaction Management Update Component', () => {
        let comp: ReturnTransactionUpdateComponent;
        let fixture: ComponentFixture<ReturnTransactionUpdateComponent>;
        let service: ReturnTransactionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [ReturnTransactionUpdateComponent]
            })
                .overrideTemplate(ReturnTransactionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReturnTransactionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReturnTransactionService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new ReturnTransaction(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.returnTransaction = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new ReturnTransaction();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.returnTransaction = entity;
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
