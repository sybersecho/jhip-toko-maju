/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { CancelTransactionDetailComponent } from 'app/entities/cancel-transaction/cancel-transaction-detail.component';
import { CancelTransaction } from 'app/shared/model/cancel-transaction.model';

describe('Component Tests', () => {
    describe('CancelTransaction Management Detail Component', () => {
        let comp: CancelTransactionDetailComponent;
        let fixture: ComponentFixture<CancelTransactionDetailComponent>;
        const route = ({ data: of({ cancelTransaction: new CancelTransaction(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [CancelTransactionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CancelTransactionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CancelTransactionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.cancelTransaction).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
