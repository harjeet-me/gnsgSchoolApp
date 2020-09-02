import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GnsgSchoolAppSharedModule } from 'app/shared/shared.module';
import { ExpenseComponent } from './expense.component';
import { ExpenseDetailComponent } from './expense-detail.component';
import { ExpenseUpdateComponent } from './expense-update.component';
import { ExpenseDeleteDialogComponent } from './expense-delete-dialog.component';
import { expenseRoute } from './expense.route';

@NgModule({
  imports: [GnsgSchoolAppSharedModule, RouterModule.forChild(expenseRoute)],
  declarations: [ExpenseComponent, ExpenseDetailComponent, ExpenseUpdateComponent, ExpenseDeleteDialogComponent],
  entryComponents: [ExpenseDeleteDialogComponent],
})
export class GnsgSchoolAppExpenseModule {}
