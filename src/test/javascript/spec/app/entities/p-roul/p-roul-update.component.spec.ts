import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { PRoulUpdateComponent } from 'app/entities/p-roul/p-roul-update.component';
import { PRoulService } from 'app/entities/p-roul/p-roul.service';
import { PRoul } from 'app/shared/model/p-roul.model';

describe('Component Tests', () => {
  describe('PRoul Management Update Component', () => {
    let comp: PRoulUpdateComponent;
    let fixture: ComponentFixture<PRoulUpdateComponent>;
    let service: PRoulService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [PRoulUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PRoulUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PRoulUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PRoulService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PRoul(123);
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
        const entity = new PRoul();
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
