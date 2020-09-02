import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IASPath } from 'app/shared/model/as-path.model';

type EntityResponseType = HttpResponse<IASPath>;
type EntityArrayResponseType = HttpResponse<IASPath[]>;

@Injectable({ providedIn: 'root' })
export class ASPathService {
  public resourceUrl = SERVER_API_URL + 'api/as-paths';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/as-paths';

  constructor(protected http: HttpClient) {}

  create(aSPath: IASPath): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aSPath);
    return this.http
      .post<IASPath>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(aSPath: IASPath): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aSPath);
    return this.http
      .put<IASPath>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IASPath>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IASPath[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IASPath[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(aSPath: IASPath): IASPath {
    const copy: IASPath = Object.assign({}, aSPath, {
      startDate: aSPath.startDate && aSPath.startDate.isValid() ? aSPath.startDate.format(DATE_FORMAT) : undefined,
      endDate: aSPath.endDate && aSPath.endDate.isValid() ? aSPath.endDate.format(DATE_FORMAT) : undefined,
      bookingDate: aSPath.bookingDate && aSPath.bookingDate.isValid() ? aSPath.bookingDate.toJSON() : undefined,
      createdDate: aSPath.createdDate && aSPath.createdDate.isValid() ? aSPath.createdDate.toJSON() : undefined,
      lastModifiedDate: aSPath.lastModifiedDate && aSPath.lastModifiedDate.isValid() ? aSPath.lastModifiedDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
      res.body.bookingDate = res.body.bookingDate ? moment(res.body.bookingDate) : undefined;
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? moment(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((aSPath: IASPath) => {
        aSPath.startDate = aSPath.startDate ? moment(aSPath.startDate) : undefined;
        aSPath.endDate = aSPath.endDate ? moment(aSPath.endDate) : undefined;
        aSPath.bookingDate = aSPath.bookingDate ? moment(aSPath.bookingDate) : undefined;
        aSPath.createdDate = aSPath.createdDate ? moment(aSPath.createdDate) : undefined;
        aSPath.lastModifiedDate = aSPath.lastModifiedDate ? moment(aSPath.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
