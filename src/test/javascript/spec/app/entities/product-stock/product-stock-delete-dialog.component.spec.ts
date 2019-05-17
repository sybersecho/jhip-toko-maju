/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhiptokomajuTestModule } from '../../../test.module';
import { ProductStockDeleteDialogComponent } from 'app/entities/product-stock/product-stock-delete-dialog.component';
import { ProductStockService } from 'app/entities/product-stock/product-stock.service';

describe('Component Tests', () => {
    describe('ProductStock Management Delete Component', () => {
        let comp: ProductStockDeleteDialogComponent;
        let fixture: ComponentFixture<ProductStockDeleteDialogComponent>;
        let service: ProductStockService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [ProductStockDeleteDialogComponent]
            })
                .overrideTemplate(ProductStockDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProductStockDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProductStockService);
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
