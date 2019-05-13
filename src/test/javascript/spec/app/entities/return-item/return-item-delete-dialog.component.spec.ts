/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhiptokomajuTestModule } from '../../../test.module';
import { ReturnItemDeleteDialogComponent } from 'app/entities/return-item/return-item-delete-dialog.component';
import { ReturnItemService } from 'app/entities/return-item/return-item.service';

describe('Component Tests', () => {
    describe('ReturnItem Management Delete Component', () => {
        let comp: ReturnItemDeleteDialogComponent;
        let fixture: ComponentFixture<ReturnItemDeleteDialogComponent>;
        let service: ReturnItemService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [ReturnItemDeleteDialogComponent]
            })
                .overrideTemplate(ReturnItemDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReturnItemDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReturnItemService);
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
