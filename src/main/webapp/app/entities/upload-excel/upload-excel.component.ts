import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {log} from "util";

@Component({
  selector: 'jhi-upload-excel',
  templateUrl: './upload-excel.component.html',
  styleUrls: ['./upload-excel.component.scss'],
})
export class UploadExcelComponent {
  files: any[] = [];

  constructor(protected http: HttpClient) {
  }

  /**
   * on file drop handler
   */
  onFileDropped($event: any): void {
    //this.prepareFilesList($event);
    this.onFileSelected($event[0])
  }

  /**
   * handle file from browsing
   */
  fileBrowseHandler(target: any): void {
    const files: any = target?.files;
    //this.prepareFilesList(files);
    this.onFileSelected(files[0])
  }

  /**
   * Delete file from files list
   * @param index (File index)
   */
  deleteFile(index: number): void {
    this.files.splice(index, 1);
  }

  onFileSelected(file: any): void {
    this.files = file ? [file] : [];
    //const file: File = event.target.files[0];
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (file) {

      const formData = new FormData();

      formData.append("file", file);

      const upload$ = this.http.post("api/import", formData);

      upload$.subscribe(res => this.files[0].progress = 100, err => this.files[0].progress = -1);

    }
  }
  /**
   * Simulate the upload process
   */
  uploadFilesSimulator(index: number): void {


    setTimeout(() => {
      if (index === this.files.length) {
        return;
      } else {
        const progressInterval = setInterval(() => {
          if (this.files[index].progress === 100) {
            clearInterval(progressInterval);
            //this.uploadFilesSimulator(index + 1);
          } else {
            //this.files[index].progress += 5;
          }
        }, 200);
        this.files[index].progress = 100;
      }
    }, 1000);
  }

  /**
   * Convert Files list to normal array list
   * @param files (Files List)
   */
  prepareFilesList(files: Array<any>): void {
    for (const item of files) {
      item.progress = 0;
      this.files.push(item);
    }

    this.uploadFilesSimulator(0);
  }

  /**
   * format bytes
   * @param bytes (File size in bytes)
   * @param decimals (Decimals point)
   */
  formatBytes(bytes?: any, decimals?: any): any {
    if (bytes === 0) {
      return '0 Bytes';
    }
    const k = 1024;
    const dm = decimals <= 0 ? 0 : decimals || 2;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)).toString() + " " + sizes[i].toString();
  }
}

