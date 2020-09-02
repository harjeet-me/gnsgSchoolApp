import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IProgram } from 'app/shared/model/program.model';

type EntityResponseType = HttpResponse<IProgram>;
type EntityArrayResponseType = HttpResponse<IProgram[]>;

@Injectable({ providedIn: 'root' })
export class ProgramService {
  public resourceUrl = SERVER_API_URL + 'api/programs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/programs';

  constructor(protected http: HttpClient) {}

  create(program: IProgram): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(program);
    return this.http
      .post<IProgram>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(program: IProgram): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(program);
    return this.http
      .put<IProgram>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProgram>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProgram[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProgram[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(program: IProgram): IProgram {
    const copy: IProgram = Object.assign({}, program, {
      etime: program.etime && program.etime.isValid() ? program.etime.toJSON() : undefined,
      langarTime: program.langarTime && program.langarTime.isValid() ? program.langarTime.toJSON() : undefined,
      bookingDate: program.bookingDate && program.bookingDate.isValid() ? program.bookingDate.toJSON() : undefined,
      createdDate: program.createdDate && program.createdDate.isValid() ? program.createdDate.toJSON() : undefined,
      lastModifiedDate: program.lastModifiedDate && program.lastModifiedDate.isValid() ? program.lastModifiedDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.etime = res.body.etime ? moment(res.body.etime) : undefined;
      res.body.langarTime = res.body.langarTime ? moment(res.body.langarTime) : undefined;
      res.body.bookingDate = res.body.bookingDate ? moment(res.body.bookingDate) : undefined;
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? moment(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((program: IProgram) => {
        program.etime = program.etime ? moment(program.etime) : undefined;
        program.langarTime = program.langarTime ? moment(program.langarTime) : undefined;
        program.bookingDate = program.bookingDate ? moment(program.bookingDate) : undefined;
        program.createdDate = program.createdDate ? moment(program.createdDate) : undefined;
        program.lastModifiedDate = program.lastModifiedDate ? moment(program.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
