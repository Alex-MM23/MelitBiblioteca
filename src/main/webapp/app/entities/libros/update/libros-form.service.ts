import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILibros, NewLibros } from '../libros.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILibros for edit and NewLibrosFormGroupInput for create.
 */
type LibrosFormGroupInput = ILibros | PartialWithRequiredKeyOf<NewLibros>;

type LibrosFormDefaults = Pick<NewLibros, 'id'>;

type LibrosFormGroupContent = {
  id: FormControl<ILibros['id'] | NewLibros['id']>;
  isbn: FormControl<ILibros['isbn']>;
  stock: FormControl<ILibros['stock']>;
  autor: FormControl<ILibros['autor']>;
  imagen: FormControl<ILibros['imagen']>;
  paginas: FormControl<ILibros['paginas']>;
  titulo: FormControl<ILibros['titulo']>;
  numeroAlquilados: FormControl<ILibros['numeroAlquilados']>;
};

export type LibrosFormGroup = FormGroup<LibrosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LibrosFormService {
  createLibrosFormGroup(libros: LibrosFormGroupInput = { id: null }): LibrosFormGroup {
    const librosRawValue = {
      ...this.getFormDefaults(),
      ...libros,
    };
    return new FormGroup<LibrosFormGroupContent>({
      id: new FormControl(
        { value: librosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      isbn: new FormControl(librosRawValue.isbn),
      stock: new FormControl(librosRawValue.stock),
      autor: new FormControl(librosRawValue.autor),
      imagen: new FormControl(librosRawValue.imagen),
      paginas: new FormControl(librosRawValue.paginas),
      titulo: new FormControl(librosRawValue.titulo),
      numeroAlquilados: new FormControl(librosRawValue.numeroAlquilados),
    });
  }

  getLibros(form: LibrosFormGroup): ILibros | NewLibros {
    return form.getRawValue() as ILibros | NewLibros;
  }

  resetForm(form: LibrosFormGroup, libros: LibrosFormGroupInput): void {
    const librosRawValue = { ...this.getFormDefaults(), ...libros };
    form.reset(
      {
        ...librosRawValue,
        id: { value: librosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LibrosFormDefaults {
    return {
      id: null,
    };
  }
}
