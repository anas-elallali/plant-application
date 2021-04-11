import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEcologicalStatus, EcologicalStatus } from '../ecological-status.model';
import { EcologicalStatusService } from '../service/ecological-status.service';

@Injectable({ providedIn: 'root' })
export class EcologicalStatusRoutingResolveService implements Resolve<IEcologicalStatus> {
  constructor(protected service: EcologicalStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEcologicalStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ecologicalStatus: HttpResponse<EcologicalStatus>) => {
          if (ecologicalStatus.body) {
            return of(ecologicalStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EcologicalStatus());
  }
}
