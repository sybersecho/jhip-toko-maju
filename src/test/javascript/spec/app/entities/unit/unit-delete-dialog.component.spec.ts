/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhiptokomajuTestModule } from '../../../test.module';
import { UnitDeleteDialogComponent } from 'app/entities/unit/unit-delete-dialog.component';
import { UnitService } from 'app/entities/unit/unit.service';

describe('Component Tests', () => {
    describe('Unit Management Delete Component', () => {
        let comp: UnitDeleteDialogComponent;
        let fixture: ComponentFixture<UnitDeleteDialogComponent>;
        let service: UnitService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [UnitDeleteDialogComponent]
            })
                .overrideTemplate(UnitDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UnitDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UnitService);
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
