import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { AppliedChargeUpdateComponent } from 'app/entities/applied-charge/applied-charge-update.component';
import { AppliedChargeService } from 'app/entities/applied-charge/applied-charge.service';
import { AppliedCharge } from 'app/shared/model/applied-charge.model';

describe('Component Tests', () => {
  describe('AppliedCharge Management Update Component', () => {
    let comp: AppliedChargeUpdateComponent;
    let fixture: ComponentFixture<AppliedChargeUpdateComponent>;
    let service: AppliedChargeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [AppliedChargeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AppliedChargeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppliedChargeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AppliedChargeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AppliedCharge(123);
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
        const entity = new AppliedCharge();
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
