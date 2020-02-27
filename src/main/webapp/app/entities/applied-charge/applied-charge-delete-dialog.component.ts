import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAppliedCharge } from 'app/shared/model/applied-charge.model';
import { AppliedChargeService } from './applied-charge.service';

@Component({
  templateUrl: './applied-charge-delete-dialog.component.html'
})
export class AppliedChargeDeleteDialogComponent {
  appliedCharge?: IAppliedCharge;

  constructor(
    protected appliedChargeService: AppliedChargeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appliedChargeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('appliedChargeListModification');
      this.activeModal.close();
    });
  }
}
