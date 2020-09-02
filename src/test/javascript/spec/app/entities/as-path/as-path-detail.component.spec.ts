import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { ASPathDetailComponent } from 'app/entities/as-path/as-path-detail.component';
import { ASPath } from 'app/shared/model/as-path.model';

describe('Component Tests', () => {
  describe('ASPath Management Detail Component', () => {
    let comp: ASPathDetailComponent;
    let fixture: ComponentFixture<ASPathDetailComponent>;
    const route = ({ data: of({ aSPath: new ASPath(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [ASPathDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ASPathDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ASPathDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aSPath on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aSPath).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
