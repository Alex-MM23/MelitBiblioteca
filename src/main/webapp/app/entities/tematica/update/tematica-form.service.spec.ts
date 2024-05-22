import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tematica.test-samples';

import { TematicaFormService } from './tematica-form.service';

describe('Tematica Form Service', () => {
  let service: TematicaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TematicaFormService);
  });

  describe('Service methods', () => {
    describe('createTematicaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTematicaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            abreviatura: expect.any(Object),
            descripcion: expect.any(Object),
          }),
        );
      });

      it('passing ITematica should create a new form with FormGroup', () => {
        const formGroup = service.createTematicaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            abreviatura: expect.any(Object),
            descripcion: expect.any(Object),
          }),
        );
      });
    });

    describe('getTematica', () => {
      it('should return NewTematica for default Tematica initial value', () => {
        const formGroup = service.createTematicaFormGroup(sampleWithNewData);

        const tematica = service.getTematica(formGroup) as any;

        expect(tematica).toMatchObject(sampleWithNewData);
      });

      it('should return NewTematica for empty Tematica initial value', () => {
        const formGroup = service.createTematicaFormGroup();

        const tematica = service.getTematica(formGroup) as any;

        expect(tematica).toMatchObject({});
      });

      it('should return ITematica', () => {
        const formGroup = service.createTematicaFormGroup(sampleWithRequiredData);

        const tematica = service.getTematica(formGroup) as any;

        expect(tematica).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITematica should not enable id FormControl', () => {
        const formGroup = service.createTematicaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTematica should disable id FormControl', () => {
        const formGroup = service.createTematicaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
