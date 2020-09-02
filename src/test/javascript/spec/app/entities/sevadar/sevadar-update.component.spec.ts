import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { SevadarUpdateComponent } from 'app/entities/sevadar/sevadar-update.component';
import { SevadarService } from 'app/entities/sevadar/sevadar.service';
import { Sevadar } from 'app/shared/model/sevadar.model';

describe('Component Tests', () => {
  describe('Sevadar Management Update Component', () => {
    let comp: SevadarUpdateComponent;
    let fixture: ComponentFixture<SevadarUpdateComponent>;
    let service: SevadarService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [SevadarUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SevadarUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SevadarUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SevadarService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Sevadar(123);
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
        const entity = new Sevadar();
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
