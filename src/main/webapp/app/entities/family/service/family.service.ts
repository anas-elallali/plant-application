import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFamily, getFamilyIdentifier } from '../family.model';

export type EntityResponseType = HttpResponse<IFamily>;
export type EntityArrayResponseType = HttpResponse<IFamily[]>;

@Injectable({ providedIn: 'root' })
export class FamilyService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/families');
  public resourcePublicUrl = this.applicationConfigService.getEndpointFor('api/public/families');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(family: IFamily): Observable<EntityResponseType> {
    return this.http.post<IFamily>(this.resourceUrl, family, { observe: 'response' });
  }

  update(family: IFamily): Observable<EntityResponseType> {
    return this.http.put<IFamily>(`${this.resourceUrl}/${getFamilyIdentifier(family) as number}`, family, { observe: 'response' });
  }

  partialUpdate(family: IFamily): Observable<EntityResponseType> {
    return this.http.patch<IFamily>(`${this.resourceUrl}/${getFamilyIdentifier(family) as number}`, family, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFamily>(`${this.resourcePublicUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFamily[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getFamilies(): Observable<EntityArrayResponseType> {
    return this.http.get<IFamily[]>(this.resourcePublicUrl, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFamilyToCollectionIfMissing(familyCollection: IFamily[], ...familiesToCheck: (IFamily | null | undefined)[]): IFamily[] {
    const families: IFamily[] = familiesToCheck.filter(isPresent);
    if (families.length > 0) {
      const familyCollectionIdentifiers = familyCollection.map(familyItem => getFamilyIdentifier(familyItem)!);
      const familiesToAdd = families.filter(familyItem => {
        const familyIdentifier = getFamilyIdentifier(familyItem);
        if (familyIdentifier == null || familyCollectionIdentifiers.includes(familyIdentifier)) {
          return false;
        }
        familyCollectionIdentifiers.push(familyIdentifier);
        return true;
      });
      return [...familiesToAdd, ...familyCollection];
    }
    return familyCollection;
  }
}
