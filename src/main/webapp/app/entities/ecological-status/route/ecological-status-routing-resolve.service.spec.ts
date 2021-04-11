jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEcologicalStatus, EcologicalStatus } from '../ecological-status.model';
import { EcologicalStatusService } from '../service/ecological-status.service';

import { EcologicalStatusRoutingResolveService } from './ecological-status-routing-resolve.service';

describe('Service Tests', () => {
  describe('EcologicalStatus routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EcologicalStatusRoutingResolveService;
    let service: EcologicalStatusService;
    let resultEcologicalStatus: IEcologicalStatus | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EcologicalStatusRoutingResolveService);
      service = TestBed.inject(EcologicalStatusService);
      resultEcologicalStatus = undefined;
    });

    describe('resolve', () => {
      it('should return IEcologicalStatus returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEcologicalStatus = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEcologicalStatus).toEqual({ id: 123 });
      });

      it('should return new IEcologicalStatus if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEcologicalStatus = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEcologicalStatus).toEqual(new EcologicalStatus());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEcologicalStatus = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEcologicalStatus).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
