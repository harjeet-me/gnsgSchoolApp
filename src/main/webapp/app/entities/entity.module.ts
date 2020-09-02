import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'student',
        loadChildren: () => import('./student/student.module').then(m => m.GnsgSchoolAppStudentModule),
      },
      {
        path: 'charge',
        loadChildren: () => import('./charge/charge.module').then(m => m.GnsgSchoolAppChargeModule),
      },
      {
        path: 'applied-charge',
        loadChildren: () => import('./applied-charge/applied-charge.module').then(m => m.GnsgSchoolAppAppliedChargeModule),
      },
      {
        path: 'program',
        loadChildren: () => import('./program/program.module').then(m => m.GnsgSchoolAppProgramModule),
      },
      {
        path: 'as-path',
        loadChildren: () => import('./as-path/as-path.module').then(m => m.GnsgSchoolAppASPathModule),
      },
      {
        path: 'expense',
        loadChildren: () => import('./expense/expense.module').then(m => m.GnsgSchoolAppExpenseModule),
      },
      {
        path: 'revenue',
        loadChildren: () => import('./revenue/revenue.module').then(m => m.GnsgSchoolAppRevenueModule),
      },
      {
        path: 'task',
        loadChildren: () => import('./task/task.module').then(m => m.GnsgSchoolAppTaskModule),
      },
      {
        path: 'sevadar',
        loadChildren: () => import('./sevadar/sevadar.module').then(m => m.GnsgSchoolAppSevadarModule),
      },
      {
        path: 'p-roul',
        loadChildren: () => import('./p-roul/p-roul.module').then(m => m.GnsgSchoolAppPRoulModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class GnsgSchoolAppEntityModule {}
