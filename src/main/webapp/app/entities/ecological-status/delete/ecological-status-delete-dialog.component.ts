import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEcologicalStatus } from '../ecological-status.model';
import { EcologicalStatusService } from '../service/ecological-status.service';

@Component({
  templateUrl: './ecological-status-delete-dialog.component.html',
})
export class EcologicalStatusDeleteDialogComponent {
  ecologicalStatus?: IEcologicalStatus;

  constructor(protected ecologicalStatusService: EcologicalStatusService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ecologicalStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
