/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { ReturnItemDetailComponent } from 'app/entities/return-item/return-item-detail.component';
import { ReturnItem } from 'app/shared/model/return-item.model';

describe('Component Tests', () => {
    describe('ReturnItem Management Detail Component', () => {
        let comp: ReturnItemDetailComponent;
        let fixture: ComponentFixture<ReturnItemDetailComponent>;
        const route = ({ data: of({ returnItem: new ReturnItem(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [ReturnItemDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReturnItemDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReturnItemDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.returnItem).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
