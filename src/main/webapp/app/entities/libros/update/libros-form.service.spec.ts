import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../libros.test-samples';

import { LibrosFormService } from './libros-form.service';

describe('Libros Form Service', () => {
  let service: LibrosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LibrosFormService);
  });

  describe('Service methods', () => {
    describe('createLibrosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLibrosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            isbn: expect.any(Object),
            stock: expect.any(Object),
            autor: expect.any(Object),
            imagen: expect.any(Object),
            paginas: expect.any(Object),
            titulo: expect.any(Object),
            numeroAlquilados: expect.any(Object),
          }),
        );
      });

      it('passing ILibros should create a new form with FormGroup', () => {
        const formGroup = service.createLibrosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            isbn: expect.any(Object),
            stock: expect.any(Object),
            autor: expect.any(Object),
            imagen: expect.any(Object),
            paginas: expect.any(Object),
            titulo: expect.any(Object),
            numeroAlquilados: expect.any(Object),
          }),
        );
      });
    });

    describe('getLibros', () => {
      it('should return NewLibros for default Libros initial value', () => {
        const formGroup = service.createLibrosFormGroup(sampleWithNewData);

        const libros = service.getLibros(formGroup) as any;

        expect(libros).toMatchObject(sampleWithNewData);
      });

      it('should return NewLibros for empty Libros initial value', () => {
        const formGroup = service.createLibrosFormGroup();

        const libros = service.getLibros(formGroup) as any;

        expect(libros).toMatchObject({});
      });

      it('should return ILibros', () => {
        const formGroup = service.createLibrosFormGroup(sampleWithRequiredData);

        const libros = service.getLibros(formGroup) as any;

        expect(libros).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILibros should not enable id FormControl', () => {
        const formGroup = service.createLibrosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLibros should disable id FormControl', () => {
        const formGroup = service.createLibrosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
