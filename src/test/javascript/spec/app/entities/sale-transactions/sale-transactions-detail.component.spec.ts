/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { SaleTransactionsDetailComponent } from 'app/entities/sale-transactions/sale-transactions-detail.component';
import { SaleTransactions } from 'app/shared/model/sale-transactions.model';

describe('Component Tests', () => {
    describe('SaleTransactions Management Detail Component', () => {
        let comp: SaleTransactionsDetailComponent;
        let fixture: ComponentFixture<SaleTransactionsDetailComponent>;
        const route = ({ data: of({ saleTransactions: new SaleTransactions(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [SaleTransactionsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SaleTransactionsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SaleTransactionsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.saleTransactions).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
