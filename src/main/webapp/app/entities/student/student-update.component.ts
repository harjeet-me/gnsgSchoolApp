import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStudent, Student } from 'app/shared/model/student.model';
import { StudentService } from './student.service';
import { IAppliedCharge } from 'app/shared/model/applied-charge.model';
import { AppliedChargeService } from 'app/entities/applied-charge/applied-charge.service';

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html'
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;
  appliedcharges: IAppliedCharge[] = [];

  editForm = this.fb.group({
    id: [],
    fullName: [],
    parentName: [],
    address: [],
    city: [],
    stateProvince: [],
    postalCode: [],
    email: [],
    phoneNumber: [],
    teacherName: [],
    active: [],
    charges: []
  });

  constructor(
    protected studentService: StudentService,
    protected appliedChargeService: AppliedChargeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      this.updateForm(student);

      this.appliedChargeService.query().subscribe((res: HttpResponse<IAppliedCharge[]>) => (this.appliedcharges = res.body || []));
    });
  }

  updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      fullName: student.fullName,
      parentName: student.parentName,
      address: student.address,
      city: student.city,
      stateProvince: student.stateProvince,
      postalCode: student.postalCode,
      email: student.email,
      phoneNumber: student.phoneNumber,
      teacherName: student.teacherName,
      active: student.active,
      charges: student.charges
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  private createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      parentName: this.editForm.get(['parentName'])!.value,
      address: this.editForm.get(['address'])!.value,
      city: this.editForm.get(['city'])!.value,
      stateProvince: this.editForm.get(['stateProvince'])!.value,
      postalCode: this.editForm.get(['postalCode'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      teacherName: this.editForm.get(['teacherName'])!.value,
      active: this.editForm.get(['active'])!.value,
      charges: this.editForm.get(['charges'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IAppliedCharge): any {
    return item.id;
  }

  getSelected(selectedVals: IAppliedCharge[], option: IAppliedCharge): IAppliedCharge {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
