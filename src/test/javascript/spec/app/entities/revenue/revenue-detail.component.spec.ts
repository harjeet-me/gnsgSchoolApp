import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { RevenueDetailComponent } from 'app/entities/revenue/revenue-detail.component';
import { Revenue } from 'app/shared/model/revenue.model';

describe('Component Tests', () => {
  describe('Revenue Management Detail Component', () => {
    let comp: RevenueDetailComponent;
    let fixture: ComponentFixture<RevenueDetailComponent>;
    const route = ({ data: of({ revenue: new Revenue(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [RevenueDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RevenueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RevenueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load revenue on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.revenue).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
