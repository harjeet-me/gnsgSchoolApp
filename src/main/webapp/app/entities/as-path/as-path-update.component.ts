import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IASPath, ASPath } from 'app/shared/model/as-path.model';
import { ASPathService } from './as-path.service';

@Component({
  selector: 'jhi-as-path-update',
  templateUrl: './as-path-update.component.html',
})
export class ASPathUpdateComponent implements OnInit {
  isSaving = false;
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    program: [],
    family: [],
    phoneNumber: [],
    address: [],
    startDate: [],
    endDate: [],
    remark: [],
    bookingDate: [],
    desc: [],
    status: [],
    createdDate: [],
    createdBy: [],
    lastModifiedDate: [],
    lastModifiedBy: [],
  });

  constructor(protected aSPathService: ASPathService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aSPath }) => {
      if (!aSPath.id) {
        const today = moment().startOf('day');
        aSPath.bookingDate = today;
        aSPath.createdDate = today;
        aSPath.lastModifiedDate = today;
      }

      this.updateForm(aSPath);
    });
  }

  updateForm(aSPath: IASPath): void {
    this.editForm.patchValue({
      id: aSPath.id,
      program: aSPath.program,
      family: aSPath.family,
      phoneNumber: aSPath.phoneNumber,
      address: aSPath.address,
      startDate: aSPath.startDate,
      endDate: aSPath.endDate,
      remark: aSPath.remark,
      bookingDate: aSPath.bookingDate ? aSPath.bookingDate.format(DATE_TIME_FORMAT) : null,
      desc: aSPath.desc,
      status: aSPath.status,
      createdDate: aSPath.createdDate ? aSPath.createdDate.format(DATE_TIME_FORMAT) : null,
      createdBy: aSPath.createdBy,
      lastModifiedDate: aSPath.lastModifiedDate ? aSPath.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: aSPath.lastModifiedBy,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aSPath = this.createFromForm();
    if (aSPath.id !== undefined) {
      this.subscribeToSaveResponse(this.aSPathService.update(aSPath));
    } else {
      this.subscribeToSaveResponse(this.aSPathService.create(aSPath));
    }
  }

  private createFromForm(): IASPath {
    return {
      ...new ASPath(),
      id: this.editForm.get(['id'])!.value,
      program: this.editForm.get(['program'])!.value,
      family: this.editForm.get(['family'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      address: this.editForm.get(['address'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      remark: this.editForm.get(['remark'])!.value,
      bookingDate: this.editForm.get(['bookingDate'])!.value
        ? moment(this.editForm.get(['bookingDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      desc: this.editForm.get(['desc'])!.value,
      status: this.editForm.get(['status'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? moment(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IASPath>>): void {
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
