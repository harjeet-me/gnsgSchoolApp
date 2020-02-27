import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'student',
        loadChildren: () => import('./student/student.module').then(m => m.GnsgSchoolAppStudentModule)
      },
      {
        path: 'charge',
        loadChildren: () => import('./charge/charge.module').then(m => m.GnsgSchoolAppChargeModule)
      },
      {
        path: 'applied-charge',
        loadChildren: () => import('./applied-charge/applied-charge.module').then(m => m.GnsgSchoolAppAppliedChargeModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class GnsgSchoolAppEntityModule {}
