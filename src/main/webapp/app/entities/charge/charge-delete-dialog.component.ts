import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICharge } from 'app/shared/model/charge.model';
import { ChargeService } from './charge.service';

@Component({
  templateUrl: './charge-delete-dialog.component.html'
})
export class ChargeDeleteDialogComponent {
  charge?: ICharge;

  constructor(protected chargeService: ChargeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chargeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('chargeListModification');
      this.activeModal.close();
    });
  }
}
