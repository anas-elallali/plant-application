jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EcologicalStatusService } from '../service/ecological-status.service';
import { IEcologicalStatus, EcologicalStatus } from '../ecological-status.model';

import { EcologicalStatusUpdateComponent } from './ecological-status-update.component';

describe('Component Tests', () => {
  describe('EcologicalStatus Management Update Component', () => {
    let comp: EcologicalStatusUpdateComponent;
    let fixture: ComponentFixture<EcologicalStatusUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ecologicalStatusService: EcologicalStatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EcologicalStatusUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EcologicalStatusUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EcologicalStatusUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ecologicalStatusService = TestBed.inject(EcologicalStatusService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const ecologicalStatus: IEcologicalStatus = { id: 456 };

        activatedRoute.data = of({ ecologicalStatus });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ecologicalStatus));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ecologicalStatus = { id: 123 };
        spyOn(ecologicalStatusService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ecologicalStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ecologicalStatus }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ecologicalStatusService.update).toHaveBeenCalledWith(ecologicalStatus);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ecologicalStatus = new EcologicalStatus();
        spyOn(ecologicalStatusService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ecologicalStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ecologicalStatus }));
        saveSubject.complete();

        // THEN
        expect(ecologicalStatusService.create).toHaveBeenCalledWith(ecologicalStatus);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ecologicalStatus = { id: 123 };
        spyOn(ecologicalStatusService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ecologicalStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ecologicalStatusService.update).toHaveBeenCalledWith(ecologicalStatus);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
