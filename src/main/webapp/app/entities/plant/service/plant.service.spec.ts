import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlant, Plant } from '../plant.model';

import { PlantService } from './plant.service';

describe('Service Tests', () => {
  describe('Plant Service', () => {
    let service: PlantService;
    let httpMock: HttpTestingController;
    let elemDefault: IPlant;
    let expectedResult: IPlant | IPlant[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PlantService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        scientificName: 'AAAAAAA',
        synonym: 'AAAAAAA',
        localName: 'AAAAAAA',
        englishName: 'AAAAAAA',
        voucherNumber: 'AAAAAAA',
        pictureContentType: 'image/png',
        picture: 'AAAAAAA',
        botanicalDescription: 'AAAAAAA',
        therapeuticUses: 'AAAAAAA',
        usedParts: 'AAAAAAA',
        preparation: 'AAAAAAA',
        pharmacologicalActivities: 'AAAAAAA',
        majorPhytochemicals: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Plant', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Plant()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Plant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            scientificName: 'BBBBBB',
            synonym: 'BBBBBB',
            localName: 'BBBBBB',
            englishName: 'BBBBBB',
            voucherNumber: 'BBBBBB',
            picture: 'BBBBBB',
            botanicalDescription: 'BBBBBB',
            therapeuticUses: 'BBBBBB',
            usedParts: 'BBBBBB',
            preparation: 'BBBBBB',
            pharmacologicalActivities: 'BBBBBB',
            majorPhytochemicals: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Plant', () => {
        const patchObject = Object.assign(
          {
            scientificName: 'BBBBBB',
            englishName: 'BBBBBB',
            voucherNumber: 'BBBBBB',
            picture: 'BBBBBB',
            usedParts: 'BBBBBB',
            preparation: 'BBBBBB',
            majorPhytochemicals: 'BBBBBB',
          },
          new Plant()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Plant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            scientificName: 'BBBBBB',
            synonym: 'BBBBBB',
            localName: 'BBBBBB',
            englishName: 'BBBBBB',
            voucherNumber: 'BBBBBB',
            picture: 'BBBBBB',
            botanicalDescription: 'BBBBBB',
            therapeuticUses: 'BBBBBB',
            usedParts: 'BBBBBB',
            preparation: 'BBBBBB',
            pharmacologicalActivities: 'BBBBBB',
            majorPhytochemicals: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Plant', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPlantToCollectionIfMissing', () => {
        it('should add a Plant to an empty array', () => {
          const plant: IPlant = { id: 123 };
          expectedResult = service.addPlantToCollectionIfMissing([], plant);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(plant);
        });

        it('should not add a Plant to an array that contains it', () => {
          const plant: IPlant = { id: 123 };
          const plantCollection: IPlant[] = [
            {
              ...plant,
            },
            { id: 456 },
          ];
          expectedResult = service.addPlantToCollectionIfMissing(plantCollection, plant);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Plant to an array that doesn't contain it", () => {
          const plant: IPlant = { id: 123 };
          const plantCollection: IPlant[] = [{ id: 456 }];
          expectedResult = service.addPlantToCollectionIfMissing(plantCollection, plant);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(plant);
        });

        it('should add only unique Plant to an array', () => {
          const plantArray: IPlant[] = [{ id: 123 }, { id: 456 }, { id: 86271 }];
          const plantCollection: IPlant[] = [{ id: 123 }];
          expectedResult = service.addPlantToCollectionIfMissing(plantCollection, ...plantArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const plant: IPlant = { id: 123 };
          const plant2: IPlant = { id: 456 };
          expectedResult = service.addPlantToCollectionIfMissing([], plant, plant2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(plant);
          expect(expectedResult).toContain(plant2);
        });

        it('should accept null and undefined values', () => {
          const plant: IPlant = { id: 123 };
          expectedResult = service.addPlantToCollectionIfMissing([], null, plant, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(plant);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
