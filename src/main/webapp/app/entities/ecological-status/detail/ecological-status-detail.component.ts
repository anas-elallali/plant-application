import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEcologicalStatus } from '../ecological-status.model';

@Component({
  selector: 'jhi-ecological-status-detail',
  templateUrl: './ecological-status-detail.component.html',
})
export class EcologicalStatusDetailComponent implements OnInit {
  ecologicalStatus: IEcologicalStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ecologicalStatus }) => {
      this.ecologicalStatus = ecologicalStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
