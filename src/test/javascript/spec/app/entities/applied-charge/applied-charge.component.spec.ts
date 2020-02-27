import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { AppliedChargeComponent } from 'app/entities/applied-charge/applied-charge.component';
import { AppliedChargeService } from 'app/entities/applied-charge/applied-charge.service';
import { AppliedCharge } from 'app/shared/model/applied-charge.model';

describe('Component Tests', () => {
  describe('AppliedCharge Management Component', () => {
    let comp: AppliedChargeComponent;
    let fixture: ComponentFixture<AppliedChargeComponent>;
    let service: AppliedChargeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [AppliedChargeComponent]
      })
        .overrideTemplate(AppliedChargeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppliedChargeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AppliedChargeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new AppliedCharge(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.appliedCharges && comp.appliedCharges[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
