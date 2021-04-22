import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PlantComponent } from './list/plant.component';
import { PlantDetailComponent } from './detail/plant-detail.component';
import { PlantUpdateComponent } from './update/plant-update.component';
import { PlantDeleteDialogComponent } from './delete/plant-delete-dialog.component';
import { PlantRoutingModule } from './route/plant-routing.module';
import {FormsModule} from "@angular/forms";

@NgModule({
  imports: [SharedModule, PlantRoutingModule, FormsModule],
  declarations: [PlantComponent, PlantDetailComponent, PlantUpdateComponent, PlantDeleteDialogComponent],
  entryComponents: [PlantDeleteDialogComponent],
})
export class PlantModule {}
