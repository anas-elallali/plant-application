import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { EcologicalStatusType } from 'app/entities/enumerations/ecological-status-type.model';
import { IEcologicalStatus, EcologicalStatus } from '../ecological-status.model';

import { EcologicalStatusService } from './ecological-status.service';

describe('Service Tests', () => {
  describe('EcologicalStatus Service', () => {
    let service: EcologicalStatusService;
    let httpMock: HttpTestingController;
    let elemDefault: IEcologicalStatus;
    let expectedResult: IEcologicalStatus | IEcologicalStatus[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EcologicalStatusService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: EcologicalStatusType.Spontaneous.toString(),
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

      it('should create a EcologicalStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EcologicalStatus()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EcologicalStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a EcologicalStatus', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new EcologicalStatus()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EcologicalStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a EcologicalStatus', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEcologicalStatusToCollectionIfMissing', () => {
        it('should add a EcologicalStatus to an empty array', () => {
          const ecologicalStatus: IEcologicalStatus = { id: 123 };
          expectedResult = service.addEcologicalStatusToCollectionIfMissing([], ecologicalStatus);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ecologicalStatus);
        });

        it('should not add a EcologicalStatus to an array that contains it', () => {
          const ecologicalStatus: IEcologicalStatus = { id: 123 };
          const ecologicalStatusCollection: IEcologicalStatus[] = [
            {
              ...ecologicalStatus,
            },
            { id: 456 },
          ];
          expectedResult = service.addEcologicalStatusToCollectionIfMissing(ecologicalStatusCollection, ecologicalStatus);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EcologicalStatus to an array that doesn't contain it", () => {
          const ecologicalStatus: IEcologicalStatus = { id: 123 };
          const ecologicalStatusCollection: IEcologicalStatus[] = [{ id: 456 }];
          expectedResult = service.addEcologicalStatusToCollectionIfMissing(ecologicalStatusCollection, ecologicalStatus);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ecologicalStatus);
        });

        it('should add only unique EcologicalStatus to an array', () => {
          const ecologicalStatusArray: IEcologicalStatus[] = [{ id: 123 }, { id: 456 }, { id: 57615 }];
          const ecologicalStatusCollection: IEcologicalStatus[] = [{ id: 123 }];
          expectedResult = service.addEcologicalStatusToCollectionIfMissing(ecologicalStatusCollection, ...ecologicalStatusArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ecologicalStatus: IEcologicalStatus = { id: 123 };
          const ecologicalStatus2: IEcologicalStatus = { id: 456 };
          expectedResult = service.addEcologicalStatusToCollectionIfMissing([], ecologicalStatus, ecologicalStatus2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ecologicalStatus);
          expect(expectedResult).toContain(ecologicalStatus2);
        });

        it('should accept null and undefined values', () => {
          const ecologicalStatus: IEcologicalStatus = { id: 123 };
          expectedResult = service.addEcologicalStatusToCollectionIfMissing([], null, ecologicalStatus, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ecologicalStatus);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
