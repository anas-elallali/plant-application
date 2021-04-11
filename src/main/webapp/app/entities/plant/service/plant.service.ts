import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlant, getPlantIdentifier } from '../plant.model';

export type EntityResponseType = HttpResponse<IPlant>;
export type EntityArrayResponseType = HttpResponse<IPlant[]>;

@Injectable({ providedIn: 'root' })
export class PlantService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/plants');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(plant: IPlant): Observable<EntityResponseType> {
    return this.http.post<IPlant>(this.resourceUrl, plant, { observe: 'response' });
  }

  update(plant: IPlant): Observable<EntityResponseType> {
    return this.http.put<IPlant>(`${this.resourceUrl}/${getPlantIdentifier(plant) as number}`, plant, { observe: 'response' });
  }

  partialUpdate(plant: IPlant): Observable<EntityResponseType> {
    return this.http.patch<IPlant>(`${this.resourceUrl}/${getPlantIdentifier(plant) as number}`, plant, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlantToCollectionIfMissing(plantCollection: IPlant[], ...plantsToCheck: (IPlant | null | undefined)[]): IPlant[] {
    const plants: IPlant[] = plantsToCheck.filter(isPresent);
    if (plants.length > 0) {
      const plantCollectionIdentifiers = plantCollection.map(plantItem => getPlantIdentifier(plantItem)!);
      const plantsToAdd = plants.filter(plantItem => {
        const plantIdentifier = getPlantIdentifier(plantItem);
        if (plantIdentifier == null || plantCollectionIdentifiers.includes(plantIdentifier)) {
          return false;
        }
        plantCollectionIdentifiers.push(plantIdentifier);
        return true;
      });
      return [...plantsToAdd, ...plantCollection];
    }
    return plantCollection;
  }
}
