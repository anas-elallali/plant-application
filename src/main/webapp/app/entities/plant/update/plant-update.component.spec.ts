jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlantService } from '../service/plant.service';
import { IPlant, Plant } from '../plant.model';
import { IEcologicalStatus } from 'app/entities/ecological-status/ecological-status.model';
import { EcologicalStatusService } from 'app/entities/ecological-status/service/ecological-status.service';
import { IFamily } from 'app/entities/family/family.model';
import { FamilyService } from 'app/entities/family/service/family.service';

import { PlantUpdateComponent } from './plant-update.component';

describe('Component Tests', () => {
  describe('Plant Management Update Component', () => {
    let comp: PlantUpdateComponent;
    let fixture: ComponentFixture<PlantUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let plantService: PlantService;
    let ecologicalStatusService: EcologicalStatusService;
    let familyService: FamilyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlantUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PlantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlantUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      plantService = TestBed.inject(PlantService);
      ecologicalStatusService = TestBed.inject(EcologicalStatusService);
      familyService = TestBed.inject(FamilyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ecologicalStatus query and add missing value', () => {
        const plant: IPlant = { id: 456 };
        const ecologicalStatus: IEcologicalStatus = { id: 4583 };
        plant.ecologicalStatus = ecologicalStatus;

        const ecologicalStatusCollection: IEcologicalStatus[] = [{ id: 1929 }];
        spyOn(ecologicalStatusService, 'query').and.returnValue(of(new HttpResponse({ body: ecologicalStatusCollection })));
        const expectedCollection: IEcologicalStatus[] = [ecologicalStatus, ...ecologicalStatusCollection];
        spyOn(ecologicalStatusService, 'addEcologicalStatusToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ plant });
        comp.ngOnInit();

        expect(ecologicalStatusService.query).toHaveBeenCalled();
        expect(ecologicalStatusService.addEcologicalStatusToCollectionIfMissing).toHaveBeenCalledWith(
          ecologicalStatusCollection,
          ecologicalStatus
        );
        expect(comp.ecologicalStatusesCollection).toEqual(expectedCollection);
      });

      it('Should call Family query and add missing value', () => {
        const plant: IPlant = { id: 456 };
        const family: IFamily = { id: 5000 };
        plant.family = family;

        const familyCollection: IFamily[] = [{ id: 21868 }];
        spyOn(familyService, 'query').and.returnValue(of(new HttpResponse({ body: familyCollection })));
        const additionalFamilies = [family];
        const expectedCollection: IFamily[] = [...additionalFamilies, ...familyCollection];
        spyOn(familyService, 'addFamilyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ plant });
        comp.ngOnInit();

        expect(familyService.query).toHaveBeenCalled();
        expect(familyService.addFamilyToCollectionIfMissing).toHaveBeenCalledWith(familyCollection, ...additionalFamilies);
        expect(comp.familiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const plant: IPlant = { id: 456 };
        const ecologicalStatus: IEcologicalStatus = { id: 93444 };
        plant.ecologicalStatus = ecologicalStatus;
        const family: IFamily = { id: 26840 };
        plant.family = family;

        activatedRoute.data = of({ plant });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(plant));
        expect(comp.ecologicalStatusesCollection).toContain(ecologicalStatus);
        expect(comp.familiesSharedCollection).toContain(family);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const plant = { id: 123 };
        spyOn(plantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ plant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: plant }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(plantService.update).toHaveBeenCalledWith(plant);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const plant = new Plant();
        spyOn(plantService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ plant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: plant }));
        saveSubject.complete();

        // THEN
        expect(plantService.create).toHaveBeenCalledWith(plant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const plant = { id: 123 };
        spyOn(plantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ plant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(plantService.update).toHaveBeenCalledWith(plant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEcologicalStatusById', () => {
        it('Should return tracked EcologicalStatus primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEcologicalStatusById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFamilyById', () => {
        it('Should return tracked Family primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFamilyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
