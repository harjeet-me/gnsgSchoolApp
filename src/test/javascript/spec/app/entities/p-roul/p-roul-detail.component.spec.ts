import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { PRoulDetailComponent } from 'app/entities/p-roul/p-roul-detail.component';
import { PRoul } from 'app/shared/model/p-roul.model';

describe('Component Tests', () => {
  describe('PRoul Management Detail Component', () => {
    let comp: PRoulDetailComponent;
    let fixture: ComponentFixture<PRoulDetailComponent>;
    const route = ({ data: of({ pRoul: new PRoul(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [PRoulDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PRoulDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PRoulDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pRoul on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pRoul).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
