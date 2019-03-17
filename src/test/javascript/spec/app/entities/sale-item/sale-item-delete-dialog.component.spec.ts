/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhiptokomajuTestModule } from '../../../test.module';
import { SaleItemDeleteDialogComponent } from 'app/entities/sale-item/sale-item-delete-dialog.component';
import { SaleItemService } from 'app/entities/sale-item/sale-item.service';

describe('Component Tests', () => {
    describe('SaleItem Management Delete Component', () => {
        let comp: SaleItemDeleteDialogComponent;
        let fixture: ComponentFixture<SaleItemDeleteDialogComponent>;
        let service: SaleItemService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [SaleItemDeleteDialogComponent]
            })
                .overrideTemplate(SaleItemDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SaleItemDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleItemService);
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
