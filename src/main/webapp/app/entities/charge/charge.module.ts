import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GnsgSchoolAppSharedModule } from 'app/shared/shared.module';
import { ChargeComponent } from './charge.component';
import { ChargeDetailComponent } from './charge-detail.component';
import { ChargeUpdateComponent } from './charge-update.component';
import { ChargeDeleteDialogComponent } from './charge-delete-dialog.component';
import { chargeRoute } from './charge.route';

@NgModule({
  imports: [GnsgSchoolAppSharedModule, RouterModule.forChild(chargeRoute)],
  declarations: [ChargeComponent, ChargeDetailComponent, ChargeUpdateComponent, ChargeDeleteDialogComponent],
  entryComponents: [ChargeDeleteDialogComponent],
})
export class GnsgSchoolAppChargeModule {}
