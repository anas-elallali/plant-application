import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlant, Plant } from '../plant.model';
import { PlantService } from '../service/plant.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEcologicalStatus } from 'app/entities/ecological-status/ecological-status.model';
import { EcologicalStatusService } from 'app/entities/ecological-status/service/ecological-status.service';
import { IFamily } from 'app/entities/family/family.model';
import { FamilyService } from 'app/entities/family/service/family.service';

@Component({
  selector: 'jhi-plant-update',
  templateUrl: './plant-update.component.html',
})
export class PlantUpdateComponent implements OnInit {
  isSaving = false;

  ecologicalStatusesCollection: IEcologicalStatus[] = [];
  familiesSharedCollection: IFamily[] = [];

  editForm = this.fb.group({
    id: [],
    scientificName: [null, [Validators.required]],
    synonym: [],
    localName: [],
    englishName: [],
    voucherNumber: [null, [Validators.required]],
    picture: [],
    pictureContentType: [],
    botanicalDescription: [],
    therapeuticUses: [],
    usedParts: [],
    preparation: [],
    pharmacologicalActivities: [],
    majorPhytochemicals: [],
    ecologicalStatus: [],
    family: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected plantService: PlantService,
    protected ecologicalStatusService: EcologicalStatusService,
    protected familyService: FamilyService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plant }) => {
      this.updateForm(plant);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('plantApplicationApp.error', { message: err.message })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plant = this.createFromForm();
    if (plant.id !== undefined) {
      this.subscribeToSaveResponse(this.plantService.update(plant));
    } else {
      this.subscribeToSaveResponse(this.plantService.create(plant));
    }
  }

  trackEcologicalStatusById(index: number, item: IEcologicalStatus): number {
    return item.id!;
  }

  trackFamilyById(index: number, item: IFamily): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlant>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(plant: IPlant): void {
    this.editForm.patchValue({
      id: plant.id,
      scientificName: plant.scientificName,
      synonym: plant.synonym,
      localName: plant.localName,
      englishName: plant.englishName,
      voucherNumber: plant.voucherNumber,
      picture: plant.picture,
      pictureContentType: plant.pictureContentType,
      botanicalDescription: plant.botanicalDescription,
      therapeuticUses: plant.therapeuticUses,
      usedParts: plant.usedParts,
      preparation: plant.preparation,
      pharmacologicalActivities: plant.pharmacologicalActivities,
      majorPhytochemicals: plant.majorPhytochemicals,
      ecologicalStatus: plant.ecologicalStatus,
      family: plant.family,
    });

    this.ecologicalStatusesCollection = this.ecologicalStatusService.addEcologicalStatusToCollectionIfMissing(
      this.ecologicalStatusesCollection,
      plant.ecologicalStatus
    );
    this.familiesSharedCollection = this.familyService.addFamilyToCollectionIfMissing(this.familiesSharedCollection, plant.family);
  }

  protected loadRelationshipsOptions(): void {
    this.ecologicalStatusService
      .query({ filter: 'plant-is-null' })
      .pipe(map((res: HttpResponse<IEcologicalStatus[]>) => res.body ?? []))
      .pipe(
        map((ecologicalStatuses: IEcologicalStatus[]) =>
          this.ecologicalStatusService.addEcologicalStatusToCollectionIfMissing(
            ecologicalStatuses,
            this.editForm.get('ecologicalStatus')!.value
          )
        )
      )
      .subscribe((ecologicalStatuses: IEcologicalStatus[]) => (this.ecologicalStatusesCollection = ecologicalStatuses));

    this.familyService
      .query()
      .pipe(map((res: HttpResponse<IFamily[]>) => res.body ?? []))
      .pipe(map((families: IFamily[]) => this.familyService.addFamilyToCollectionIfMissing(families, this.editForm.get('family')!.value)))
      .subscribe((families: IFamily[]) => (this.familiesSharedCollection = families));
  }

  protected createFromForm(): IPlant {
    return {
      ...new Plant(),
      id: this.editForm.get(['id'])!.value,
      scientificName: this.editForm.get(['scientificName'])!.value,
      synonym: this.editForm.get(['synonym'])!.value,
      localName: this.editForm.get(['localName'])!.value,
      englishName: this.editForm.get(['englishName'])!.value,
      voucherNumber: this.editForm.get(['voucherNumber'])!.value,
      pictureContentType: this.editForm.get(['pictureContentType'])!.value,
      picture: this.editForm.get(['picture'])!.value,
      botanicalDescription: this.editForm.get(['botanicalDescription'])!.value,
      therapeuticUses: this.editForm.get(['therapeuticUses'])!.value,
      usedParts: this.editForm.get(['usedParts'])!.value,
      preparation: this.editForm.get(['preparation'])!.value,
      pharmacologicalActivities: this.editForm.get(['pharmacologicalActivities'])!.value,
      majorPhytochemicals: this.editForm.get(['majorPhytochemicals'])!.value,
      ecologicalStatus: this.editForm.get(['ecologicalStatus'])!.value,
      family: this.editForm.get(['family'])!.value,
    };
  }
}
