import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'family',
        data: { pageTitle: 'Families' },
        loadChildren: () => import('./family/family.module').then(m => m.FamilyModule),
      },
      {
        path: 'ecological-status',
        data: { pageTitle: 'EcologicalStatuses' },
        loadChildren: () => import('./ecological-status/ecological-status.module').then(m => m.EcologicalStatusModule),
      },
      {
        path: 'plant',
        data: { pageTitle: 'Plants' },
        loadChildren: () => import('./plant/plant.module').then(m => m.PlantModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
