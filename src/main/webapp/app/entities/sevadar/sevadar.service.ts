import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ISevadar } from 'app/shared/model/sevadar.model';

type EntityResponseType = HttpResponse<ISevadar>;
type EntityArrayResponseType = HttpResponse<ISevadar[]>;

@Injectable({ providedIn: 'root' })
export class SevadarService {
  public resourceUrl = SERVER_API_URL + 'api/sevadars';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/sevadars';

  constructor(protected http: HttpClient) {}

  create(sevadar: ISevadar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sevadar);
    return this.http
      .post<ISevadar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sevadar: ISevadar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sevadar);
    return this.http
      .put<ISevadar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISevadar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISevadar[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISevadar[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(sevadar: ISevadar): ISevadar {
    const copy: ISevadar = Object.assign({}, sevadar, {
      sevaStartDate: sevadar.sevaStartDate && sevadar.sevaStartDate.isValid() ? sevadar.sevaStartDate.toJSON() : undefined,
      sevaEndDate: sevadar.sevaEndDate && sevadar.sevaEndDate.isValid() ? sevadar.sevaEndDate.toJSON() : undefined,
      createdDate: sevadar.createdDate && sevadar.createdDate.isValid() ? sevadar.createdDate.toJSON() : undefined,
      lastModifiedDate: sevadar.lastModifiedDate && sevadar.lastModifiedDate.isValid() ? sevadar.lastModifiedDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.sevaStartDate = res.body.sevaStartDate ? moment(res.body.sevaStartDate) : undefined;
      res.body.sevaEndDate = res.body.sevaEndDate ? moment(res.body.sevaEndDate) : undefined;
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? moment(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sevadar: ISevadar) => {
        sevadar.sevaStartDate = sevadar.sevaStartDate ? moment(sevadar.sevaStartDate) : undefined;
        sevadar.sevaEndDate = sevadar.sevaEndDate ? moment(sevadar.sevaEndDate) : undefined;
        sevadar.createdDate = sevadar.createdDate ? moment(sevadar.createdDate) : undefined;
        sevadar.lastModifiedDate = sevadar.lastModifiedDate ? moment(sevadar.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
