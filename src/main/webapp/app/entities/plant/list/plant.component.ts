import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlant } from '../plant.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PlantService } from '../service/plant.service';
import { PlantDeleteDialogComponent } from '../delete/plant-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-plant',
  templateUrl: './plant.component.html',
  styleUrls: ['./plant.component.scss'],
})
export class PlantComponent implements OnInit {
  plants?: IPlant[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  familyId!: any;
  familyName: any = "";

  value = '';
  searchKey = 'scientificName';
  constructor(
    protected plantService: PlantService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.activatedRoute.paramMap.subscribe(params => {
      this.familyId  = params.get('id') ?  parseInt(params.get('id')!, 10) : null;

      if(this.familyId){
        let query: any = {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          value: this.value,
          searchKey: this.searchKey
        };

        if(this.searchKey && this.value) {
          query = {
            ...query,
            value: this.value,
            searchKey: this.searchKey
          }
        }
        this.plantService
          .findAllByFamilyId(this.familyId, query)
          .subscribe(
            (res: HttpResponse<IPlant[]>) => {
              this.isLoading = false;
              this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
            },
            () => {
              this.isLoading = false;
              this.onError();
            }
          );
      }

    });
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IPlant): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(plant: IPlant): void {
    const modalRef = this.modalService.open(PlantDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plant = plant;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IPlant[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;

    this.familyName = (data && data.length > 0) ? data[0]?.family?.name : "";
    if (navigate) {
      this.router.navigate(['/plant/family', this.familyId], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.plants = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
