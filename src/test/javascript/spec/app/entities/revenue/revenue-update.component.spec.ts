import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GnsgSchoolAppTestModule } from '../../../test.module';
import { RevenueUpdateComponent } from 'app/entities/revenue/revenue-update.component';
import { RevenueService } from 'app/entities/revenue/revenue.service';
import { Revenue } from 'app/shared/model/revenue.model';

describe('Component Tests', () => {
  describe('Revenue Management Update Component', () => {
    let comp: RevenueUpdateComponent;
    let fixture: ComponentFixture<RevenueUpdateComponent>;
    let service: RevenueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GnsgSchoolAppTestModule],
        declarations: [RevenueUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RevenueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RevenueUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RevenueService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Revenue(123);
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
        const entity = new Revenue();
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
