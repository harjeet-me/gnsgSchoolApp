import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IExpense, Expense } from 'app/shared/model/expense.model';
import { ExpenseService } from './expense.service';

@Component({
  selector: 'jhi-expense-update',
  templateUrl: './expense-update.component.html',
})
export class ExpenseUpdateComponent implements OnInit {
  isSaving = false;
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    expType: [],
    amt: [],
    date: [],
    desc: [],
    createdDate: [],
    createdBy: [],
    lastModifiedDate: [],
    lastModifiedBy: [],
  });

  constructor(protected expenseService: ExpenseService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expense }) => {
      if (!expense.id) {
        const today = moment().startOf('day');
        expense.createdDate = today;
        expense.lastModifiedDate = today;
      }

      this.updateForm(expense);
    });
  }

  updateForm(expense: IExpense): void {
    this.editForm.patchValue({
      id: expense.id,
      expType: expense.expType,
      amt: expense.amt,
      date: expense.date,
      desc: expense.desc,
      createdDate: expense.createdDate ? expense.createdDate.format(DATE_TIME_FORMAT) : null,
      createdBy: expense.createdBy,
      lastModifiedDate: expense.lastModifiedDate ? expense.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: expense.lastModifiedBy,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expense = this.createFromForm();
    if (expense.id !== undefined) {
      this.subscribeToSaveResponse(this.expenseService.update(expense));
    } else {
      this.subscribeToSaveResponse(this.expenseService.create(expense));
    }
  }

  private createFromForm(): IExpense {
    return {
      ...new Expense(),
      id: this.editForm.get(['id'])!.value,
      expType: this.editForm.get(['expType'])!.value,
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpense>>): void {
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
