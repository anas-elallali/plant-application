jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FamilyService } from '../service/family.service';
import { IFamily, Family } from '../family.model';

import { FamilyUpdateComponent } from './family-update.component';

describe('Component Tests', () => {
  describe('Family Management Update Component', () => {
    let comp: FamilyUpdateComponent;
    let fixture: ComponentFixture<FamilyUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let familyService: FamilyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FamilyUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FamilyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FamilyUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      familyService = TestBed.inject(FamilyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const family: IFamily = { id: 456 };

        activatedRoute.data = of({ family });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(family));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const family = { id: 123 };
        spyOn(familyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ family });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: family }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(familyService.update).toHaveBeenCalledWith(family);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const family = new Family();
        spyOn(familyService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ family });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: family }));
        saveSubject.complete();

        // THEN
        expect(familyService.create).toHaveBeenCalledWith(family);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const family = { id: 123 };
        spyOn(familyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ family });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(familyService.update).toHaveBeenCalledWith(family);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
