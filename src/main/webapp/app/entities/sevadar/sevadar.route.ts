import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISevadar, Sevadar } from 'app/shared/model/sevadar.model';
import { SevadarService } from './sevadar.service';
import { SevadarComponent } from './sevadar.component';
import { SevadarDetailComponent } from './sevadar-detail.component';
import { SevadarUpdateComponent } from './sevadar-update.component';

@Injectable({ providedIn: 'root' })
export class SevadarResolve implements Resolve<ISevadar> {
  constructor(private service: SevadarService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISevadar> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((sevadar: HttpResponse<Sevadar>) => {
          if (sevadar.body) {
            return of(sevadar.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sevadar());
  }
}

export const sevadarRoute: Routes = [
  {
    path: '',
    component: SevadarComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams,
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gnsgSchoolApp.sevadar.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SevadarDetailComponent,
    resolve: {
      sevadar: SevadarResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.sevadar.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SevadarUpdateComponent,
    resolve: {
      sevadar: SevadarResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.sevadar.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SevadarUpdateComponent,
    resolve: {
      sevadar: SevadarResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.sevadar.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
