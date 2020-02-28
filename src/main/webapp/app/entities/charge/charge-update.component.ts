import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICharge, Charge } from 'app/shared/model/charge.model';
import { ChargeService } from './charge.service';
import { IStudent } from 'app/shared/model/student.model';
import { StudentService } from 'app/entities/student/student.service';

@Component({
  selector: 'jhi-charge-update',
  templateUrl: './charge-update.component.html'
})
export class ChargeUpdateComponent implements OnInit {
  isSaving = false;
  students: IStudent[] = [];
  dueDateDp: any;
  paymentDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [],
    amt: [],
    month: [],
    dueDate: [],
    paymentDate: [],
    amtPaid: [],
    ref: [],
    student: []
  });

  constructor(
    protected chargeService: ChargeService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ charge }) => {
      this.updateForm(charge);

      this.studentService.query().subscribe((res: HttpResponse<IStudent[]>) => (this.students = res.body || []));
    });
  }

  updateForm(charge: ICharge): void {
    this.editForm.patchValue({
      id: charge.id,
      name: charge.name,
      amt: charge.amt,
      month: charge.month,
      dueDate: charge.dueDate,
      paymentDate: charge.paymentDate,
      amtPaid: charge.amtPaid,
      ref: charge.ref,
      student: charge.student
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const charge = this.createFromForm();
    if (charge.id !== undefined) {
      this.subscribeToSaveResponse(this.chargeService.update(charge));
    } else {
      this.subscribeToSaveResponse(this.chargeService.create(charge));
    }
  }

  private createFromForm(): ICharge {
    return {
      ...new Charge(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      amt: this.editForm.get(['amt'])!.value,
      month: this.editForm.get(['month'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value,
      amtPaid: this.editForm.get(['amtPaid'])!.value,
      ref: this.editForm.get(['ref'])!.value,
      student: this.editForm.get(['student'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICharge>>): void {
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

  trackById(index: number, item: IStudent): any {
    return item.id;
  }
}
