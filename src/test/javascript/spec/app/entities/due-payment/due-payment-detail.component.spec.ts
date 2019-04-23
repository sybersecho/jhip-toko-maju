/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { DuePaymentDetailComponent } from 'app/entities/due-payment/due-payment-detail.component';
import { DuePayment } from 'app/shared/model/due-payment.model';

describe('Component Tests', () => {
    describe('DuePayment Management Detail Component', () => {
        let comp: DuePaymentDetailComponent;
        let fixture: ComponentFixture<DuePaymentDetailComponent>;
        const route = ({ data: of({ duePayment: new DuePayment(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [DuePaymentDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DuePaymentDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DuePaymentDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.duePayment).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
