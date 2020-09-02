import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IASPath } from 'app/shared/model/as-path.model';
import { ASPathService } from './as-path.service';

@Component({
  templateUrl: './as-path-delete-dialog.component.html',
})
export class ASPathDeleteDialogComponent {
  aSPath?: IASPath;

  constructor(protected aSPathService: ASPathService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aSPathService.delete(id).subscribe(() => {
      this.eventManager.broadcast('aSPathListModification');
      this.activeModal.close();
    });
  }
}
