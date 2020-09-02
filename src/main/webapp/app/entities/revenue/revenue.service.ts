import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IRevenue } from 'app/shared/model/revenue.model';

type EntityResponseType = HttpResponse<IRevenue>;
type EntityArrayResponseType = HttpResponse<IRevenue[]>;

@Injectable({ providedIn: 'root' })
export class RevenueService {
  public resourceUrl = SERVER_API_URL + 'api/revenues';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/revenues';

  constructor(protected http: HttpClient) {}

  create(revenue: IRevenue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(revenue);
    return this.http
      .post<IRevenue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(revenue: IRevenue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(revenue);
    return this.http
      .put<IRevenue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRevenue>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRevenue[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRevenue[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(revenue: IRevenue): IRevenue {
    const copy: IRevenue = Object.assign({}, revenue, {
      date: revenue.date && revenue.date.isValid() ? revenue.date.format(DATE_FORMAT) : undefined,
      createdDate: revenue.createdDate && revenue.createdDate.isValid() ? revenue.createdDate.toJSON() : undefined,
      lastModifiedDate: revenue.lastModifiedDate && revenue.lastModifiedDate.isValid() ? revenue.lastModifiedDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? moment(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((revenue: IRevenue) => {
        revenue.date = revenue.date ? moment(revenue.date) : undefined;
        revenue.createdDate = revenue.createdDate ? moment(revenue.createdDate) : undefined;
        revenue.lastModifiedDate = revenue.lastModifiedDate ? moment(revenue.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
