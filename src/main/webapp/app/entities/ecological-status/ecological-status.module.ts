import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EcologicalStatusComponent } from './list/ecological-status.component';
import { EcologicalStatusDetailComponent } from './detail/ecological-status-detail.component';
import { EcologicalStatusUpdateComponent } from './update/ecological-status-update.component';
import { EcologicalStatusDeleteDialogComponent } from './delete/ecological-status-delete-dialog.component';
import { EcologicalStatusRoutingModule } from './route/ecological-status-routing.module';

@NgModule({
  imports: [SharedModule, EcologicalStatusRoutingModule],
  declarations: [
    EcologicalStatusComponent,
    EcologicalStatusDetailComponent,
    EcologicalStatusUpdateComponent,
    EcologicalStatusDeleteDialogComponent,
  ],
  entryComponents: [EcologicalStatusDeleteDialogComponent],
})
export class EcologicalStatusModule {}
