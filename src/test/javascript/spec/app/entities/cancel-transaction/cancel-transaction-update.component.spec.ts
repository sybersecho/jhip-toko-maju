/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { CancelTransactionUpdateComponent } from 'app/entities/cancel-transaction/cancel-transaction-update.component';
import { CancelTransactionService } from 'app/entities/cancel-transaction/cancel-transaction.service';
import { CancelTransaction } from 'app/shared/model/cancel-transaction.model';

describe('Component Tests', () => {
    describe('CancelTransaction Management Update Component', () => {
        let comp: CancelTransactionUpdateComponent;
        let fixture: ComponentFixture<CancelTransactionUpdateComponent>;
        let service: CancelTransactionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [CancelTransactionUpdateComponent]
            })
                .overrideTemplate(CancelTransactionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CancelTransactionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CancelTransactionService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new CancelTransaction(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.cancelTransaction = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new CancelTransaction();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.cancelTransaction = entity;
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
