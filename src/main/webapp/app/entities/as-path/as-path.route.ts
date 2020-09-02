import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IASPath, ASPath } from 'app/shared/model/as-path.model';
import { ASPathService } from './as-path.service';
import { ASPathComponent } from './as-path.component';
import { ASPathDetailComponent } from './as-path-detail.component';
import { ASPathUpdateComponent } from './as-path-update.component';

@Injectable({ providedIn: 'root' })
export class ASPathResolve implements Resolve<IASPath> {
  constructor(private service: ASPathService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IASPath> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((aSPath: HttpResponse<ASPath>) => {
          if (aSPath.body) {
            return of(aSPath.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ASPath());
  }
}

export const aSPathRoute: Routes = [
  {
    path: '',
    component: ASPathComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams,
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gnsgSchoolApp.aSPath.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ASPathDetailComponent,
    resolve: {
      aSPath: ASPathResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.aSPath.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ASPathUpdateComponent,
    resolve: {
      aSPath: ASPathResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.aSPath.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ASPathUpdateComponent,
    resolve: {
      aSPath: ASPathResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.aSPath.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
