import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { ChargeComponent } from 'app/entities/charge/charge.component';
import { ChargeService } from 'app/entities/charge/charge.service';
import { Charge } from 'app/shared/model/charge.model';

describe('Component Tests', () => {
  describe('Charge Management Component', () => {
    let comp: ChargeComponent;
    let fixture: ComponentFixture<ChargeComponent>;
    let service: ChargeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [ChargeComponent]
      })
        .overrideTemplate(ChargeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChargeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChargeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Charge(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.charges && comp.charges[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
