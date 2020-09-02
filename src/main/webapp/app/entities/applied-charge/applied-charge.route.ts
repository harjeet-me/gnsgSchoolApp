import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAppliedCharge, AppliedCharge } from 'app/shared/model/applied-charge.model';
import { AppliedChargeService } from './applied-charge.service';
import { AppliedChargeComponent } from './applied-charge.component';
import { AppliedChargeDetailComponent } from './applied-charge-detail.component';
import { AppliedChargeUpdateComponent } from './applied-charge-update.component';

@Injectable({ providedIn: 'root' })
export class AppliedChargeResolve implements Resolve<IAppliedCharge> {
  constructor(private service: AppliedChargeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppliedCharge> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((appliedCharge: HttpResponse<AppliedCharge>) => {
          if (appliedCharge.body) {
            return of(appliedCharge.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AppliedCharge());
  }
}

export const appliedChargeRoute: Routes = [
  {
    path: '',
    component: AppliedChargeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.appliedCharge.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppliedChargeDetailComponent,
    resolve: {
      appliedCharge: AppliedChargeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.appliedCharge.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppliedChargeUpdateComponent,
    resolve: {
      appliedCharge: AppliedChargeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.appliedCharge.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppliedChargeUpdateComponent,
    resolve: {
      appliedCharge: AppliedChargeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.appliedCharge.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
