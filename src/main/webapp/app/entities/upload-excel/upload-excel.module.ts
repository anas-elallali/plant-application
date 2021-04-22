import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { UploadExcelComponent } from './upload-excel.component';
import {DndDirective} from "app/entities/upload-excel/dnd.directive";
import {ProgressComponent} from "app/entities/upload-excel/progress/progress.component";

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild([{
      path: '',
      component: UploadExcelComponent,
      data: {
        pageTitle: 'Upload Excel',
      },
    }]),
  ],
  declarations: [DndDirective, UploadExcelComponent, ProgressComponent],
})
export class UploadExcelModule {}
