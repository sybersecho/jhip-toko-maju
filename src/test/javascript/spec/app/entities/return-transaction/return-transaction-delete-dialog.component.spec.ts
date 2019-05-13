/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhiptokomajuTestModule } from '../../../test.module';
import { ReturnTransactionDeleteDialogComponent } from 'app/entities/return-transaction/return-transaction-delete-dialog.component';
import { ReturnTransactionService } from 'app/entities/return-transaction/return-transaction.service';

describe('Component Tests', () => {
    describe('ReturnTransaction Management Delete Component', () => {
        let comp: ReturnTransactionDeleteDialogComponent;
        let fixture: ComponentFixture<ReturnTransactionDeleteDialogComponent>;
        let service: ReturnTransactionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [ReturnTransactionDeleteDialogComponent]
            })
                .overrideTemplate(ReturnTransactionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReturnTransactionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReturnTransactionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
