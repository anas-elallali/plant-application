import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEcologicalStatus, getEcologicalStatusIdentifier } from '../ecological-status.model';

export type EntityResponseType = HttpResponse<IEcologicalStatus>;
export type EntityArrayResponseType = HttpResponse<IEcologicalStatus[]>;

@Injectable({ providedIn: 'root' })
export class EcologicalStatusService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/ecological-statuses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(ecologicalStatus: IEcologicalStatus): Observable<EntityResponseType> {
    return this.http.post<IEcologicalStatus>(this.resourceUrl, ecologicalStatus, { observe: 'response' });
  }

  update(ecologicalStatus: IEcologicalStatus): Observable<EntityResponseType> {
    return this.http.put<IEcologicalStatus>(
      `${this.resourceUrl}/${getEcologicalStatusIdentifier(ecologicalStatus) as number}`,
      ecologicalStatus,
      { observe: 'response' }
    );
  }

  partialUpdate(ecologicalStatus: IEcologicalStatus): Observable<EntityResponseType> {
    return this.http.patch<IEcologicalStatus>(
      `${this.resourceUrl}/${getEcologicalStatusIdentifier(ecologicalStatus) as number}`,
      ecologicalStatus,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEcologicalStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEcologicalStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEcologicalStatusToCollectionIfMissing(
    ecologicalStatusCollection: IEcologicalStatus[],
    ...ecologicalStatusesToCheck: (IEcologicalStatus | null | undefined)[]
  ): IEcologicalStatus[] {
    const ecologicalStatuses: IEcologicalStatus[] = ecologicalStatusesToCheck.filter(isPresent);
    if (ecologicalStatuses.length > 0) {
      const ecologicalStatusCollectionIdentifiers = ecologicalStatusCollection.map(
        ecologicalStatusItem => getEcologicalStatusIdentifier(ecologicalStatusItem)!
      );
      const ecologicalStatusesToAdd = ecologicalStatuses.filter(ecologicalStatusItem => {
        const ecologicalStatusIdentifier = getEcologicalStatusIdentifier(ecologicalStatusItem);
        if (ecologicalStatusIdentifier == null || ecologicalStatusCollectionIdentifiers.includes(ecologicalStatusIdentifier)) {
          return false;
        }
        ecologicalStatusCollectionIdentifiers.push(ecologicalStatusIdentifier);
        return true;
      });
      return [...ecologicalStatusesToAdd, ...ecologicalStatusCollection];
    }
    return ecologicalStatusCollection;
  }
}
