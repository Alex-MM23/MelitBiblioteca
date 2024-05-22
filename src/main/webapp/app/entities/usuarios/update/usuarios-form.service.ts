import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUsuarios, NewUsuarios } from '../usuarios.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUsuarios for edit and NewUsuariosFormGroupInput for create.
 */
type UsuariosFormGroupInput = IUsuarios | PartialWithRequiredKeyOf<NewUsuarios>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUsuarios | NewUsuarios> = Omit<T, 'fechaAlta'> & {
  fechaAlta?: string | null;
};

type UsuariosFormRawValue = FormValueOf<IUsuarios>;

type NewUsuariosFormRawValue = FormValueOf<NewUsuarios>;

type UsuariosFormDefaults = Pick<NewUsuarios, 'id' | 'fechaAlta'>;

type UsuariosFormGroupContent = {
  id: FormControl<UsuariosFormRawValue['id'] | NewUsuarios['id']>;
  username: FormControl<UsuariosFormRawValue['username']>;
  password: FormControl<UsuariosFormRawValue['password']>;
  email: FormControl<UsuariosFormRawValue['email']>;
  nombre: FormControl<UsuariosFormRawValue['nombre']>;
  apellido: FormControl<UsuariosFormRawValue['apellido']>;
  direccion: FormControl<UsuariosFormRawValue['direccion']>;
  fechaAlta: FormControl<UsuariosFormRawValue['fechaAlta']>;
};

export type UsuariosFormGroup = FormGroup<UsuariosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UsuariosFormService {
  createUsuariosFormGroup(usuarios: UsuariosFormGroupInput = { id: null }): UsuariosFormGroup {
    const usuariosRawValue = this.convertUsuariosToUsuariosRawValue({
      ...this.getFormDefaults(),
      ...usuarios,
    });
    return new FormGroup<UsuariosFormGroupContent>({
      id: new FormControl(
        { value: usuariosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      username: new FormControl(usuariosRawValue.username),
      password: new FormControl(usuariosRawValue.password),
      email: new FormControl(usuariosRawValue.email),
      nombre: new FormControl(usuariosRawValue.nombre),
      apellido: new FormControl(usuariosRawValue.apellido),
      direccion: new FormControl(usuariosRawValue.direccion),
      fechaAlta: new FormControl(usuariosRawValue.fechaAlta),
    });
  }

  getUsuarios(form: UsuariosFormGroup): IUsuarios | NewUsuarios {
    return this.convertUsuariosRawValueToUsuarios(form.getRawValue() as UsuariosFormRawValue | NewUsuariosFormRawValue);
  }

  resetForm(form: UsuariosFormGroup, usuarios: UsuariosFormGroupInput): void {
    const usuariosRawValue = this.convertUsuariosToUsuariosRawValue({ ...this.getFormDefaults(), ...usuarios });
    form.reset(
      {
        ...usuariosRawValue,
        id: { value: usuariosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UsuariosFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaAlta: currentTime,
    };
  }

  private convertUsuariosRawValueToUsuarios(rawUsuarios: UsuariosFormRawValue | NewUsuariosFormRawValue): IUsuarios | NewUsuarios {
    return {
      ...rawUsuarios,
      fechaAlta: dayjs(rawUsuarios.fechaAlta, DATE_TIME_FORMAT),
    };
  }

  private convertUsuariosToUsuariosRawValue(
    usuarios: IUsuarios | (Partial<NewUsuarios> & UsuariosFormDefaults),
  ): UsuariosFormRawValue | PartialWithRequiredKeyOf<NewUsuariosFormRawValue> {
    return {
      ...usuarios,
      fechaAlta: usuarios.fechaAlta ? usuarios.fechaAlta.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
