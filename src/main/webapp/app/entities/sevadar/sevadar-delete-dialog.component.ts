import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISevadar } from 'app/shared/model/sevadar.model';
import { SevadarService } from './sevadar.service';

@Component({
  templateUrl: './sevadar-delete-dialog.component.html',
})
export class SevadarDeleteDialogComponent {
  sevadar?: ISevadar;

  constructor(protected sevadarService: SevadarService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sevadarService.delete(id).subscribe(() => {
      this.eventManager.broadcast('sevadarListModification');
      this.activeModal.close();
    });
  }
}
