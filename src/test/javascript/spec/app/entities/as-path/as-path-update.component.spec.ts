import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { ASPathUpdateComponent } from 'app/entities/as-path/as-path-update.component';
import { ASPathService } from 'app/entities/as-path/as-path.service';
import { ASPath } from 'app/shared/model/as-path.model';

describe('Component Tests', () => {
  describe('ASPath Management Update Component', () => {
    let comp: ASPathUpdateComponent;
    let fixture: ComponentFixture<ASPathUpdateComponent>;
    let service: ASPathService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [ASPathUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ASPathUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ASPathUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ASPathService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ASPath(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ASPath();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
