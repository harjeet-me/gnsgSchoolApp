import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { ChargeDetailComponent } from 'app/entities/charge/charge-detail.component';
import { Charge } from 'app/shared/model/charge.model';

describe('Component Tests', () => {
  describe('Charge Management Detail Component', () => {
    let comp: ChargeDetailComponent;
    let fixture: ComponentFixture<ChargeDetailComponent>;
    const route = ({ data: of({ charge: new Charge(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [ChargeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ChargeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChargeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load charge on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.charge).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
