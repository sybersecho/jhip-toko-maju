/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhiptokomajuTestModule } from '../../../test.module';
import { SaleTransactionsDeleteDialogComponent } from 'app/entities/sale-transactions/sale-transactions-delete-dialog.component';
import { SaleTransactionsService } from 'app/entities/sale-transactions/sale-transactions.service';

describe('Component Tests', () => {
    describe('SaleTransactions Management Delete Component', () => {
        let comp: SaleTransactionsDeleteDialogComponent;
        let fixture: ComponentFixture<SaleTransactionsDeleteDialogComponent>;
        let service: SaleTransactionsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [SaleTransactionsDeleteDialogComponent]
            })
                .overrideTemplate(SaleTransactionsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SaleTransactionsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleTransactionsService);
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
