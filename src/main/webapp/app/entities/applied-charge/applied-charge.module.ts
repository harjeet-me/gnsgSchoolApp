import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GnsgSchoolAppSharedModule } from 'app/shared/shared.module';
import { AppliedChargeComponent } from './applied-charge.component';
import { AppliedChargeDetailComponent } from './applied-charge-detail.component';
import { AppliedChargeUpdateComponent } from './applied-charge-update.component';
import { AppliedChargeDeleteDialogComponent } from './applied-charge-delete-dialog.component';
import { appliedChargeRoute } from './applied-charge.route';

@NgModule({
  imports: [GnsgSchoolAppSharedModule, RouterModule.forChild(appliedChargeRoute)],
  declarations: [AppliedChargeComponent, AppliedChargeDetailComponent, AppliedChargeUpdateComponent, AppliedChargeDeleteDialogComponent],
  entryComponents: [AppliedChargeDeleteDialogComponent]
})
export class GnsgSchoolAppAppliedChargeModule {}
