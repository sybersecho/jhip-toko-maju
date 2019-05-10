/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhiptokomajuTestModule } from '../../../test.module';
import { UnitDetailComponent } from 'app/entities/unit/unit-detail.component';
import { Unit } from 'app/shared/model/unit.model';

describe('Component Tests', () => {
    describe('Unit Management Detail Component', () => {
        let comp: UnitDetailComponent;
        let fixture: ComponentFixture<UnitDetailComponent>;
        const route = ({ data: of({ unit: new Unit(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [JhiptokomajuTestModule],
                declarations: [UnitDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UnitDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UnitDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.unit).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
