import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPRoul } from 'app/shared/model/p-roul.model';
import { PRoulService } from './p-roul.service';

@Component({
  templateUrl: './p-roul-delete-dialog.component.html',
})
export class PRoulDeleteDialogComponent {
  pRoul?: IPRoul;

  constructor(protected pRoulService: PRoulService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pRoulService.delete(id).subscribe(() => {
      this.eventManager.broadcast('pRoulListModification');
      this.activeModal.close();
    });
  }
}
