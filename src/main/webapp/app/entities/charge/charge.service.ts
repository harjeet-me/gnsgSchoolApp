import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { ICharge } from 'app/shared/model/charge.model';

type EntityResponseType = HttpResponse<ICharge>;
type EntityArrayResponseType = HttpResponse<ICharge[]>;

@Injectable({ providedIn: 'root' })
export class ChargeService {
  public resourceUrl = SERVER_API_URL + 'api/charges';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/charges';

  constructor(protected http: HttpClient) {}

  create(charge: ICharge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(charge);
    return this.http
      .post<ICharge>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(charge: ICharge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(charge);
    return this.http
      .put<ICharge>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICharge>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICharge[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICharge[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(charge: ICharge): ICharge {
    const copy: ICharge = Object.assign({}, charge, {
      dueDate: charge.dueDate && charge.dueDate.isValid() ? charge.dueDate.format(DATE_FORMAT) : undefined,
      paymentDate: charge.paymentDate && charge.paymentDate.isValid() ? charge.paymentDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dueDate = res.body.dueDate ? moment(res.body.dueDate) : undefined;
      res.body.paymentDate = res.body.paymentDate ? moment(res.body.paymentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((charge: ICharge) => {
        charge.dueDate = charge.dueDate ? moment(charge.dueDate) : undefined;
        charge.paymentDate = charge.paymentDate ? moment(charge.paymentDate) : undefined;
      });
    }
    return res;
  }
}
