import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EcologicalStatusComponent } from '../list/ecological-status.component';
import { EcologicalStatusDetailComponent } from '../detail/ecological-status-detail.component';
import { EcologicalStatusUpdateComponent } from '../update/ecological-status-update.component';
import { EcologicalStatusRoutingResolveService } from './ecological-status-routing-resolve.service';

const ecologicalStatusRoute: Routes = [
  {
    path: '',
    component: EcologicalStatusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EcologicalStatusDetailComponent,
    resolve: {
      ecologicalStatus: EcologicalStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EcologicalStatusUpdateComponent,
    resolve: {
      ecologicalStatus: EcologicalStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EcologicalStatusUpdateComponent,
    resolve: {
      ecologicalStatus: EcologicalStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ecologicalStatusRoute)],
  exports: [RouterModule],
})
export class EcologicalStatusRoutingModule {}
