import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { LibrosService } from '../service/libros.service';
import { ILibros } from '../libros.model';
import { LibrosFormService } from './libros-form.service';

import { LibrosUpdateComponent } from './libros-update.component';

describe('Libros Management Update Component', () => {
  let comp: LibrosUpdateComponent;
  let fixture: ComponentFixture<LibrosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let librosFormService: LibrosFormService;
  let librosService: LibrosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, LibrosUpdateComponent],
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
      .overrideTemplate(LibrosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LibrosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    librosFormService = TestBed.inject(LibrosFormService);
    librosService = TestBed.inject(LibrosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const libros: ILibros = { id: 456 };

      activatedRoute.data = of({ libros });
      comp.ngOnInit();

      expect(comp.libros).toEqual(libros);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILibros>>();
      const libros = { id: 123 };
      jest.spyOn(librosFormService, 'getLibros').mockReturnValue(libros);
      jest.spyOn(librosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ libros });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: libros }));
      saveSubject.complete();

      // THEN
      expect(librosFormService.getLibros).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(librosService.update).toHaveBeenCalledWith(expect.objectContaining(libros));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILibros>>();
      const libros = { id: 123 };
      jest.spyOn(librosFormService, 'getLibros').mockReturnValue({ id: null });
      jest.spyOn(librosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ libros: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: libros }));
      saveSubject.complete();

      // THEN
      expect(librosFormService.getLibros).toHaveBeenCalled();
      expect(librosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILibros>>();
      const libros = { id: 123 };
      jest.spyOn(librosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ libros });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(librosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
