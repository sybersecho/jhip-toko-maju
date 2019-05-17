/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhiptokomajuTestModule } from '../../../test.module';
import { BadStockProductDeleteDialogComponent } from 'app/entities/bad-stock-product/bad-stock-product-delete-dialog.component';
import { BadStockProductService } from 'app/entities/bad-stock-product/bad-stock-product.service';

describe('Component Tests', () => {
    describe('BadStockProduct Management Delete Component', () => {
        let comp: BadStockProductDeleteDialogComponent;
        let fixture: ComponentFixture<BadStockProductDeleteDialogComponent>;
        let service: BadStockProductService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [BadStockProductDeleteDialogComponent]
            })
                .overrideTemplate(BadStockProductDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BadStockProductDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BadStockProductService);
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
