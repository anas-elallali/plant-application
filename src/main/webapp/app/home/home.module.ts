import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import {MatButtonModule} from "@angular/material/button";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatListModule} from "@angular/material/list";

@NgModule({
  imports: [
    SharedModule,
    MatButtonModule,
    MatExpansionModule,
    MatListModule,
    RouterModule.forChild([HOME_ROUTE]),
  ],
  declarations: [HomeComponent],
})
export class HomeModule {}
