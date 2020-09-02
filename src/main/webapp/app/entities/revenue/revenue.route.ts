import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRevenue, Revenue } from 'app/shared/model/revenue.model';
import { RevenueService } from './revenue.service';
import { RevenueComponent } from './revenue.component';
import { RevenueDetailComponent } from './revenue-detail.component';
import { RevenueUpdateComponent } from './revenue-update.component';

@Injectable({ providedIn: 'root' })
export class RevenueResolve implements Resolve<IRevenue> {
  constructor(private service: RevenueService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRevenue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((revenue: HttpResponse<Revenue>) => {
          if (revenue.body) {
            return of(revenue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Revenue());
  }
}

export const revenueRoute: Routes = [
  {
    path: '',
    component: RevenueComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.revenue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RevenueDetailComponent,
    resolve: {
      revenue: RevenueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.revenue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RevenueUpdateComponent,
    resolve: {
      revenue: RevenueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.revenue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RevenueUpdateComponent,
    resolve: {
      revenue: RevenueResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.revenue.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
