/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { SaleTransactionsUpdateComponent } from 'app/entities/sale-transactions/sale-transactions-update.component';
import { SaleTransactionsService } from 'app/entities/sale-transactions/sale-transactions.service';
import { SaleTransactions } from 'app/shared/model/sale-transactions.model';

describe('Component Tests', () => {
    describe('SaleTransactions Management Update Component', () => {
        let comp: SaleTransactionsUpdateComponent;
        let fixture: ComponentFixture<SaleTransactionsUpdateComponent>;
        let service: SaleTransactionsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [SaleTransactionsUpdateComponent]
            })
                .overrideTemplate(SaleTransactionsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SaleTransactionsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleTransactionsService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SaleTransactions(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.saleTransactions = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SaleTransactions();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.saleTransactions = entity;
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
