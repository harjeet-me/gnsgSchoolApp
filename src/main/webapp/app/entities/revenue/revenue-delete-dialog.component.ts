import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRevenue } from 'app/shared/model/revenue.model';
import { RevenueService } from './revenue.service';

@Component({
  templateUrl: './revenue-delete-dialog.component.html',
})
export class RevenueDeleteDialogComponent {
  revenue?: IRevenue;

  constructor(protected revenueService: RevenueService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.revenueService.delete(id).subscribe(() => {
      this.eventManager.broadcast('revenueListModification');
      this.activeModal.close();
    });
  }
}
