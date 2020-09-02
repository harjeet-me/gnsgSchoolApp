import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GnsgSchoolAppSharedModule } from 'app/shared/shared.module';
import { ASPathComponent } from './as-path.component';
import { ASPathDetailComponent } from './as-path-detail.component';
import { ASPathUpdateComponent } from './as-path-update.component';
import { ASPathDeleteDialogComponent } from './as-path-delete-dialog.component';
import { aSPathRoute } from './as-path.route';

@NgModule({
  imports: [GnsgSchoolAppSharedModule, RouterModule.forChild(aSPathRoute)],
  declarations: [ASPathComponent, ASPathDetailComponent, ASPathUpdateComponent, ASPathDeleteDialogComponent],
  entryComponents: [ASPathDeleteDialogComponent],
})
export class GnsgSchoolAppASPathModule {}
