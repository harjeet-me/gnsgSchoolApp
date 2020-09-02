import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { SevadarDetailComponent } from 'app/entities/sevadar/sevadar-detail.component';
import { Sevadar } from 'app/shared/model/sevadar.model';

describe('Component Tests', () => {
  describe('Sevadar Management Detail Component', () => {
    let comp: SevadarDetailComponent;
    let fixture: ComponentFixture<SevadarDetailComponent>;
    const route = ({ data: of({ sevadar: new Sevadar(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [SevadarDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SevadarDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SevadarDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sevadar on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sevadar).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
