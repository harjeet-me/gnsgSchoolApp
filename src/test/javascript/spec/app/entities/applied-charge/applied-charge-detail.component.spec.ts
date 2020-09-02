import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { AppliedChargeDetailComponent } from 'app/entities/applied-charge/applied-charge-detail.component';
import { AppliedCharge } from 'app/shared/model/applied-charge.model';

describe('Component Tests', () => {
  describe('AppliedCharge Management Detail Component', () => {
    let comp: AppliedChargeDetailComponent;
    let fixture: ComponentFixture<AppliedChargeDetailComponent>;
    const route = ({ data: of({ appliedCharge: new AppliedCharge(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [AppliedChargeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AppliedChargeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AppliedChargeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load appliedCharge on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.appliedCharge).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
