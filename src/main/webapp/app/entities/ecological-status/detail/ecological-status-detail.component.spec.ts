import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EcologicalStatusDetailComponent } from './ecological-status-detail.component';

describe('Component Tests', () => {
  describe('EcologicalStatus Management Detail Component', () => {
    let comp: EcologicalStatusDetailComponent;
    let fixture: ComponentFixture<EcologicalStatusDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EcologicalStatusDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ecologicalStatus: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EcologicalStatusDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EcologicalStatusDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ecologicalStatus on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ecologicalStatus).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
