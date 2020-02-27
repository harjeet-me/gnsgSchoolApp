import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IAppliedCharge } from 'app/shared/model/applied-charge.model';

type EntityResponseType = HttpResponse<IAppliedCharge>;
type EntityArrayResponseType = HttpResponse<IAppliedCharge[]>;

@Injectable({ providedIn: 'root' })
export class AppliedChargeService {
  public resourceUrl = SERVER_API_URL + 'api/applied-charges';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/applied-charges';

  constructor(protected http: HttpClient) {}

  create(appliedCharge: IAppliedCharge): Observable<EntityResponseType> {
    return this.http.post<IAppliedCharge>(this.resourceUrl, appliedCharge, { observe: 'response' });
  }

  update(appliedCharge: IAppliedCharge): Observable<EntityResponseType> {
    return this.http.put<IAppliedCharge>(this.resourceUrl, appliedCharge, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAppliedCharge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppliedCharge[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppliedCharge[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
