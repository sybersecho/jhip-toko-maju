/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { ReturnTransactionDetailComponent } from 'app/entities/return-transaction/return-transaction-detail.component';
import { ReturnTransaction } from 'app/shared/model/return-transaction.model';

describe('Component Tests', () => {
    describe('ReturnTransaction Management Detail Component', () => {
        let comp: ReturnTransactionDetailComponent;
        let fixture: ComponentFixture<ReturnTransactionDetailComponent>;
        const route = ({ data: of({ returnTransaction: new ReturnTransaction(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [ReturnTransactionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReturnTransactionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReturnTransactionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.returnTransaction).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
