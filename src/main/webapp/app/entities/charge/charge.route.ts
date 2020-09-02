import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICharge, Charge } from 'app/shared/model/charge.model';
import { ChargeService } from './charge.service';
import { ChargeComponent } from './charge.component';
import { ChargeDetailComponent } from './charge-detail.component';
import { ChargeUpdateComponent } from './charge-update.component';

@Injectable({ providedIn: 'root' })
export class ChargeResolve implements Resolve<ICharge> {
  constructor(private service: ChargeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICharge> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((charge: HttpResponse<Charge>) => {
          if (charge.body) {
            return of(charge.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Charge());
  }
}

export const chargeRoute: Routes = [
  {
    path: '',
    component: ChargeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.charge.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChargeDetailComponent,
    resolve: {
      charge: ChargeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.charge.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChargeUpdateComponent,
    resolve: {
      charge: ChargeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.charge.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChargeUpdateComponent,
    resolve: {
      charge: ChargeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gnsgSchoolApp.charge.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
