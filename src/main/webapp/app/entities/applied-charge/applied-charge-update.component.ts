import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAppliedCharge, AppliedCharge } from 'app/shared/model/applied-charge.model';
import { AppliedChargeService } from './applied-charge.service';

@Component({
  selector: 'jhi-applied-charge-update',
  templateUrl: './applied-charge-update.component.html',
})
export class AppliedChargeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    type: [],
    ammount: [],
    dueInterval: [],
  });

  constructor(protected appliedChargeService: AppliedChargeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appliedCharge }) => {
      this.updateForm(appliedCharge);
    });
  }

  updateForm(appliedCharge: IAppliedCharge): void {
    this.editForm.patchValue({
      id: appliedCharge.id,
      type: appliedCharge.type,
      ammount: appliedCharge.ammount,
      dueInterval: appliedCharge.dueInterval,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appliedCharge = this.createFromForm();
    if (appliedCharge.id !== undefined) {
      this.subscribeToSaveResponse(this.appliedChargeService.update(appliedCharge));
    } else {
      this.subscribeToSaveResponse(this.appliedChargeService.create(appliedCharge));
    }
  }

  private createFromForm(): IAppliedCharge {
    return {
      ...new AppliedCharge(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      ammount: this.editForm.get(['ammount'])!.value,
      dueInterval: this.editForm.get(['dueInterval'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppliedCharge>>): void {
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
}
