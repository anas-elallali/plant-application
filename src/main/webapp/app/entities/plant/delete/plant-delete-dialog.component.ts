import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlant } from '../plant.model';
import { PlantService } from '../service/plant.service';

@Component({
  templateUrl: './plant-delete-dialog.component.html',
})
export class PlantDeleteDialogComponent {
  plant?: IPlant;

  constructor(protected plantService: PlantService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.plantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
