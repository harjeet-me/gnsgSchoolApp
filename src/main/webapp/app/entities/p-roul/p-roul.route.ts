import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPRoul, PRoul } from 'app/shared/model/p-roul.model';
import { PRoulService } from './p-roul.service';
import { PRoulComponent } from './p-roul.component';
import { PRoulDetailComponent } from './p-roul-detail.component';
import { PRoulUpdateComponent } from './p-roul-update.component';

@Injectable({ providedIn: 'root' })
export class PRoulResolve implements Resolve<IPRoul> {
  constructor(private service: PRoulService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPRoul> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pRoul: HttpResponse<PRoul>) => {
          if (pRoul.body) {
            return of(pRoul.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PRoul());
  }
}

export const pRoulRoute: Routes = [
  {
    path: '',
    component: PRoulComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams,
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gnsgSchoolApp.pRoul.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PRoulDetailComponent,
    resolve: {
      pRoul: PRoulResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.pRoul.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PRoulUpdateComponent,
    resolve: {
      pRoul: PRoulResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.pRoul.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PRoulUpdateComponent,
    resolve: {
      pRoul: PRoulResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.pRoul.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
