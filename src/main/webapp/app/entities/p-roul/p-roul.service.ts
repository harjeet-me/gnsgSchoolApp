import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IPRoul } from 'app/shared/model/p-roul.model';

type EntityResponseType = HttpResponse<IPRoul>;
type EntityArrayResponseType = HttpResponse<IPRoul[]>;

@Injectable({ providedIn: 'root' })
export class PRoulService {
  public resourceUrl = SERVER_API_URL + 'api/p-rouls';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/p-rouls';

  constructor(protected http: HttpClient) {}

  create(pRoul: IPRoul): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pRoul);
    return this.http
      .post<IPRoul>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pRoul: IPRoul): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pRoul);
    return this.http
      .put<IPRoul>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPRoul>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPRoul[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPRoul[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(pRoul: IPRoul): IPRoul {
    const copy: IPRoul = Object.assign({}, pRoul, {
      createdDate: pRoul.createdDate && pRoul.createdDate.isValid() ? pRoul.createdDate.toJSON() : undefined,
      lastModifiedDate: pRoul.lastModifiedDate && pRoul.lastModifiedDate.isValid() ? pRoul.lastModifiedDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? moment(res.body.createdDate) : undefined;
      res.body.lastModifiedDate = res.body.lastModifiedDate ? moment(res.body.lastModifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pRoul: IPRoul) => {
        pRoul.createdDate = pRoul.createdDate ? moment(pRoul.createdDate) : undefined;
        pRoul.lastModifiedDate = pRoul.lastModifiedDate ? moment(pRoul.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
