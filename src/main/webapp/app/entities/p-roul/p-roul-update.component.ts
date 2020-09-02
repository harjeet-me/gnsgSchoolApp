import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPRoul, PRoul } from 'app/shared/model/p-roul.model';
import { PRoulService } from './p-roul.service';
import { ISevadar } from 'app/shared/model/sevadar.model';
import { SevadarService } from 'app/entities/sevadar/sevadar.service';
import { IASPath } from 'app/shared/model/as-path.model';
import { ASPathService } from 'app/entities/as-path/as-path.service';

type SelectableEntity = ISevadar | IASPath;

@Component({
  selector: 'jhi-p-roul-update',
  templateUrl: './p-roul-update.component.html',
})
export class PRoulUpdateComponent implements OnInit {
  isSaving = false;
  pathis: ISevadar[] = [];
  aspaths: IASPath[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    desc: [],
    totalRoul: [],
    createdDate: [],
    createdBy: [],
    lastModifiedDate: [],
    lastModifiedBy: [],
    pathi: [],
    sehajPath: [],
  });

  constructor(
    protected pRoulService: PRoulService,
    protected sevadarService: SevadarService,
    protected aSPathService: ASPathService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pRoul }) => {
      if (!pRoul.id) {
        const today = moment().startOf('day');
        pRoul.createdDate = today;
        pRoul.lastModifiedDate = today;
      }

      this.updateForm(pRoul);

      this.sevadarService
        .query({ filter: 'proul-is-null' })
        .pipe(
          map((res: HttpResponse<ISevadar[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ISevadar[]) => {
          if (!pRoul.pathi || !pRoul.pathi.id) {
            this.pathis = resBody;
          } else {
            this.sevadarService
              .find(pRoul.pathi.id)
              .pipe(
                map((subRes: HttpResponse<ISevadar>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ISevadar[]) => (this.pathis = concatRes));
          }
        });

      this.aSPathService.query().subscribe((res: HttpResponse<IASPath[]>) => (this.aspaths = res.body || []));
    });
  }

  updateForm(pRoul: IPRoul): void {
    this.editForm.patchValue({
      id: pRoul.id,
      name: pRoul.name,
      desc: pRoul.desc,
      totalRoul: pRoul.totalRoul,
      createdDate: pRoul.createdDate ? pRoul.createdDate.format(DATE_TIME_FORMAT) : null,
      createdBy: pRoul.createdBy,
      lastModifiedDate: pRoul.lastModifiedDate ? pRoul.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: pRoul.lastModifiedBy,
      pathi: pRoul.pathi,
      sehajPath: pRoul.sehajPath,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pRoul = this.createFromForm();
    if (pRoul.id !== undefined) {
      this.subscribeToSaveResponse(this.pRoulService.update(pRoul));
    } else {
      this.subscribeToSaveResponse(this.pRoulService.create(pRoul));
    }
  }

  private createFromForm(): IPRoul {
    return {
      ...new PRoul(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      desc: this.editForm.get(['desc'])!.value,
      totalRoul: this.editForm.get(['totalRoul'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value
        ? moment(this.editForm.get(['createdDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate'])!.value
        ? moment(this.editForm.get(['lastModifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy'])!.value,
      pathi: this.editForm.get(['pathi'])!.value,
      sehajPath: this.editForm.get(['sehajPath'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPRoul>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
