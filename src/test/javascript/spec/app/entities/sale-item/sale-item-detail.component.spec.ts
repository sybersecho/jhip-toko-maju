/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { SaleItemDetailComponent } from 'app/entities/sale-item/sale-item-detail.component';
import { SaleItem } from 'app/shared/model/sale-item.model';

describe('Component Tests', () => {
    describe('SaleItem Management Detail Component', () => {
        let comp: SaleItemDetailComponent;
        let fixture: ComponentFixture<SaleItemDetailComponent>;
        const route = ({ data: of({ saleItem: new SaleItem(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [SaleItemDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SaleItemDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SaleItemDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.saleItem).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
