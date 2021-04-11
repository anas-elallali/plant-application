import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEcologicalStatus, EcologicalStatus } from '../ecological-status.model';
import { EcologicalStatusService } from '../service/ecological-status.service';

@Component({
  selector: 'jhi-ecological-status-update',
  templateUrl: './ecological-status-update.component.html',
})
export class EcologicalStatusUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(
    protected ecologicalStatusService: EcologicalStatusService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ecologicalStatus }) => {
      this.updateForm(ecologicalStatus);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ecologicalStatus = this.createFromForm();
    if (ecologicalStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.ecologicalStatusService.update(ecologicalStatus));
    } else {
      this.subscribeToSaveResponse(this.ecologicalStatusService.create(ecologicalStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEcologicalStatus>>): void {
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

  protected updateForm(ecologicalStatus: IEcologicalStatus): void {
    this.editForm.patchValue({
      id: ecologicalStatus.id,
      name: ecologicalStatus.name,
    });
  }

  protected createFromForm(): IEcologicalStatus {
    return {
      ...new EcologicalStatus(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
