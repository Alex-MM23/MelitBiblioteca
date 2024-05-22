import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { TematicaService } from '../service/tematica.service';
import { ITematica } from '../tematica.model';
import { TematicaFormService } from './tematica-form.service';

import { TematicaUpdateComponent } from './tematica-update.component';

describe('Tematica Management Update Component', () => {
  let comp: TematicaUpdateComponent;
  let fixture: ComponentFixture<TematicaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tematicaFormService: TematicaFormService;
  let tematicaService: TematicaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TematicaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TematicaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TematicaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tematicaFormService = TestBed.inject(TematicaFormService);
    tematicaService = TestBed.inject(TematicaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tematica: ITematica = { id: 456 };

      activatedRoute.data = of({ tematica });
      comp.ngOnInit();

      expect(comp.tematica).toEqual(tematica);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITematica>>();
      const tematica = { id: 123 };
      jest.spyOn(tematicaFormService, 'getTematica').mockReturnValue(tematica);
      jest.spyOn(tematicaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tematica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tematica }));
      saveSubject.complete();

      // THEN
      expect(tematicaFormService.getTematica).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tematicaService.update).toHaveBeenCalledWith(expect.objectContaining(tematica));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITematica>>();
      const tematica = { id: 123 };
      jest.spyOn(tematicaFormService, 'getTematica').mockReturnValue({ id: null });
      jest.spyOn(tematicaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tematica: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tematica }));
      saveSubject.complete();

      // THEN
      expect(tematicaFormService.getTematica).toHaveBeenCalled();
      expect(tematicaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITematica>>();
      const tematica = { id: 123 };
      jest.spyOn(tematicaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tematica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tematicaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
