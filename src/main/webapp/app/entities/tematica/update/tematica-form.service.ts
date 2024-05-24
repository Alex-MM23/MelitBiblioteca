import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITematica, NewTematica } from '../tematica.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITematica for edit and NewTematicaFormGroupInput for create.
 */
type TematicaFormGroupInput = ITematica | PartialWithRequiredKeyOf<NewTematica>;

type TematicaFormDefaults = Pick<NewTematica, 'id'>;

type TematicaFormGroupContent = {
  id: FormControl<ITematica['id'] | NewTematica['id']>;
  abreviatura: FormControl<ITematica['abreviatura']>;
  descripcion: FormControl<ITematica['descripcion']>;
};

export type TematicaFormGroup = FormGroup<TematicaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TematicaFormService {
  createTematicaFormGroup(tematica: TematicaFormGroupInput = { id: null }): TematicaFormGroup {
    const tematicaRawValue = {
      ...this.getFormDefaults(),
      ...tematica,
    };
    return new FormGroup<TematicaFormGroupContent>({
      id: new FormControl(
        { value: tematicaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      abreviatura: new FormControl(tematicaRawValue.abreviatura),
      descripcion: new FormControl(tematicaRawValue.descripcion),
    });
  }

  getTematica(form: TematicaFormGroup): ITematica | NewTematica {
    return form.getRawValue() as ITematica | NewTematica;
  }

  resetForm(form: TematicaFormGroup, tematica: TematicaFormGroupInput): void {
    const tematicaRawValue = { ...this.getFormDefaults(), ...tematica };
    form.reset(
      {
        ...tematicaRawValue,
        id: { value: tematicaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TematicaFormDefaults {
    return {
      id: null,
    };
  }
}
