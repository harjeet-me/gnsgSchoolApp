import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IExpense } from 'app/shared/model/expense.model';

type EntityResponseType = HttpResponse<IExpense>;
type EntityArrayResponseType = HttpResponse<IExpense[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseService {
  public resourceUrl = SERVER_API_URL + 'api/expenses';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/expenses';

  constructor(protected http: HttpClient) {}

  create(expense: IExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expense);
    return this.http
      .post<IExpense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(expense: IExpense): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expense);
    return this.http
      .put<IExpense>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExpense[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExpense[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(expense: IExpense): IExpense {
    const copy: IExpense = Object.assign({}, expense, {
      date: expense.date && expense.date.isValid() ? expense.date.format(DATE_FORMAT) : undefined,
      createdDate: expense.createdDate && expense.createdDate.isValid() ? expense.createdDate.toJSON() : undefined,
      lastModifiedDate: expense.lastModifiedDate && expense.lastModifiedDate.isValid() ? expense.lastModifiedDate.toJSON() : undefined,
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
      res.body.forEach((expense: IExpense) => {
        expense.date = expense.date ? moment(expense.date) : undefined;
        expense.createdDate = expense.createdDate ? moment(expense.createdDate) : undefined;
        expense.lastModifiedDate = expense.lastModifiedDate ? moment(expense.lastModifiedDate) : undefined;
      });
    }
    return res;
  }
}
