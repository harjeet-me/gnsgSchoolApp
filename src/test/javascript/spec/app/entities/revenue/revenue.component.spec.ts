import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { RevenueComponent } from 'app/entities/revenue/revenue.component';
import { RevenueService } from 'app/entities/revenue/revenue.service';
import { Revenue } from 'app/shared/model/revenue.model';

describe('Component Tests', () => {
  describe('Revenue Management Component', () => {
    let comp: RevenueComponent;
    let fixture: ComponentFixture<RevenueComponent>;
    let service: RevenueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [RevenueComponent],
      })
        .overrideTemplate(RevenueComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RevenueComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RevenueService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Revenue(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.revenues && comp.revenues[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
