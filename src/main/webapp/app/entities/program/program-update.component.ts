import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProgram, Program } from 'app/shared/model/program.model';
import { ProgramService } from './program.service';
import { ISevadar } from 'app/shared/model/sevadar.model';
import { SevadarService } from 'app/entities/sevadar/sevadar.service';

@Component({
  selector: 'jhi-program-update',
  templateUrl: './program-update.component.html',
})
export class ProgramUpdateComponent implements OnInit {
  isSaving = false;
  sevadars: ISevadar[] = [];

  editForm = this.fb.group({
    id: [],
    programType: [],
    location: [],
    etime: [],
    family: [],
    phoneNumber: [],
    address: [],
    withLangar: [],
    langarMenu: [],
    langarTime: [],
    dueAmt: [],
    paidAmt: [],
    balAmt: [],
    recieptNumber: [],
    remark: [],
    bookingDate: [],
    status: [],
    createdDate: [],
    createdBy: [],
    lastModifiedDate: [],
    lastModifiedBy: [],
    sevadar: [],
  });

  constructor(
    protected programService: ProgramService,
    protected sevadarService: SevadarService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ program }) => {
      if (!program.id) {
        const today = moment().startOf('day');
        program.etime = today;
        program.langarTime = today;
        program.bookingDate = today;
        program.createdDate = today;
        program.lastModifiedDate = today;
      }

      this.updateForm(program);

      this.sevadarService.query().subscribe((res: HttpResponse<ISevadar[]>) => (this.sevadars = res.body || []));
    });
  }

  updateForm(program: IProgram): void {
    this.editForm.patchValue({
      id: program.id,
      programType: program.programType,
      location: program.location,
      etime: program.etime ? program.etime.format(DATE_TIME_FORMAT) : null,
      family: program.family,
      phoneNumber: program.phoneNumber,
      address: program.address,
      withLangar: program.withLangar,
      langarMenu: program.langarMenu,
      langarTime: program.langarTime ? program.langarTime.format(DATE_TIME_FORMAT) : null,
      dueAmt: program.dueAmt,
      paidAmt: program.paidAmt,
      balAmt: program.balAmt,
      recieptNumber: program.recieptNumber,
      remark: program.remark,
      bookingDate: program.bookingDate ? program.bookingDate.format(DATE_TIME_FORMAT) : null,
      status: program.status,
      createdDate: program.createdDate ? program.createdDate.format(DATE_TIME_FORMAT) : null,
      createdBy: program.createdBy,
      lastModifiedDate: program.lastModifiedDate ? program.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: program.lastModifiedBy,
      sevadar: program.sevadar,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const program = this.createFromForm();
    if (program.id !== undefined) {
      this.subscribeToSaveResponse(this.programService.update(program));
    } else {
      this.subscribeToSaveResponse(this.programService.create(program));
    }
  }

  private createFromForm(): IProgram {
    return {
      ...new Program(),
      id: this.editForm.get(['id'])!.value,
      programType: this.editForm.get(['programType'])!.value,
      location: this.editForm.get(['location'])!.value,
      etime: this.editForm.get(['etime'])!.value ? moment(this.editForm.get(['etime'])!.value, DATE_TIME_FORMAT) : undefined,
      family: this.editForm.get(['family'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      address: this.editForm.get(['address'])!.value,
      withLangar: this.editForm.get(['withLangar'])!.value,
      langarMenu: this.editForm.get(['langarMenu'])!.value,
      langarTime: this.editForm.get(['langarTime'])!.value ? moment(this.editForm.get(['langarTime'])!.value, DATE_TIME_FORMAT) : undefined,
      dueAmt: this.editForm.get(['dueAmt'])!.value,
      paidAmt: this.editForm.get(['paidAmt'])!.value,
      balAmt: this.editForm.get(['balAmt'])!.value,
      recieptNumber: this.editForm.get(['recieptNumber'])!.value,
      remark: this.editForm.get(['remark'])!.value,
      bookingDate: this.editForm.get(['bookingDate'])!.value
        ? moment(this.editForm.get(['bookingDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? moment(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      sevadar: this.editForm.get(['sevadar'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProgram>>): void {
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

  trackById(index: number, item: ISevadar): any {
    return item.id;
  }
}
