/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhiptokomajuTestModule } from '../../../test.module';
import { CancelTransactionDeleteDialogComponent } from 'app/entities/cancel-transaction/cancel-transaction-delete-dialog.component';
import { CancelTransactionService } from 'app/entities/cancel-transaction/cancel-transaction.service';

describe('Component Tests', () => {
    describe('CancelTransaction Management Delete Component', () => {
        let comp: CancelTransactionDeleteDialogComponent;
        let fixture: ComponentFixture<CancelTransactionDeleteDialogComponent>;
        let service: CancelTransactionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [CancelTransactionDeleteDialogComponent]
            })
                .overrideTemplate(CancelTransactionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CancelTransactionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CancelTransactionService);
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
