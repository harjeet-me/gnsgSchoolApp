import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRevenue, Revenue } from 'app/shared/model/revenue.model';
import { RevenueService } from './revenue.service';

@Component({
  selector: 'jhi-revenue-update',
  templateUrl: './revenue-update.component.html',
})
export class RevenueUpdateComponent implements OnInit {
  isSaving = false;
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    revType: [],
    amt: [],
    date: [],
    desc: [],
    createdDate: [],
    createdBy: [],
    lastModifiedDate: [],
    lastModifiedBy: [],
  });

  constructor(protected revenueService: RevenueService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ revenue }) => {
      if (!revenue.id) {
        const today = moment().startOf('day');
        revenue.createdDate = today;
        revenue.lastModifiedDate = today;
      }

      this.updateForm(revenue);
    });
  }

  updateForm(revenue: IRevenue): void {
    this.editForm.patchValue({
      id: revenue.id,
      revType: revenue.revType,
      amt: revenue.amt,
      date: revenue.date,
      desc: revenue.desc,
      createdDate: revenue.createdDate ? revenue.createdDate.format(DATE_TIME_FORMAT) : null,
      createdBy: revenue.createdBy,
      lastModifiedDate: revenue.lastModifiedDate ? revenue.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: revenue.lastModifiedBy,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const revenue = this.createFromForm();
    if (revenue.id !== undefined) {
      this.subscribeToSaveResponse(this.revenueService.update(revenue));
    } else {
      this.subscribeToSaveResponse(this.revenueService.create(revenue));
    }
  }

  private createFromForm(): IRevenue {
    return {
      ...new Revenue(),
      id: this.editForm.get(['id'])!.value,
      revType: this.editForm.get(['revType'])!.value,
      amt: this.editForm.get(['amt'])!.value,
      date: this.editForm.get(['date'])!.value,
      desc: this.editForm.get(['desc'])!.value,
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRevenue>>): void {
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
