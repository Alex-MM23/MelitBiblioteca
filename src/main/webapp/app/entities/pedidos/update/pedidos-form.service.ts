import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPedidos, NewPedidos } from '../pedidos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPedidos for edit and NewPedidosFormGroupInput for create.
 */
type PedidosFormGroupInput = IPedidos | PartialWithRequiredKeyOf<NewPedidos>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPedidos | NewPedidos> = Omit<T, 'fechaAlta' | 'fechaEntrega'> & {
  fechaAlta?: string | null;
  fechaEntrega?: string | null;
};

type PedidosFormRawValue = FormValueOf<IPedidos>;

type NewPedidosFormRawValue = FormValueOf<NewPedidos>;

type PedidosFormDefaults = Pick<NewPedidos, 'id' | 'fechaAlta' | 'fechaEntrega'>;

type PedidosFormGroupContent = {
  id: FormControl<PedidosFormRawValue['id'] | NewPedidos['id']>;
  direccion: FormControl<PedidosFormRawValue['direccion']>;
  fechaAlta: FormControl<PedidosFormRawValue['fechaAlta']>;
  fechaEntrega: FormControl<PedidosFormRawValue['fechaEntrega']>;
  username: FormControl<PedidosFormRawValue['username']>;
};

export type PedidosFormGroup = FormGroup<PedidosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PedidosFormService {
  createPedidosFormGroup(pedidos: PedidosFormGroupInput = { id: null }): PedidosFormGroup {
    const pedidosRawValue = this.convertPedidosToPedidosRawValue({
      ...this.getFormDefaults(),
      ...pedidos,
    });
    return new FormGroup<PedidosFormGroupContent>({
      id: new FormControl(
        { value: pedidosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      direccion: new FormControl(pedidosRawValue.direccion),
      fechaAlta: new FormControl(pedidosRawValue.fechaAlta),
      fechaEntrega: new FormControl(pedidosRawValue.fechaEntrega),
      username: new FormControl(pedidosRawValue.username),
    });
  }

  getPedidos(form: PedidosFormGroup): IPedidos | NewPedidos {
    return this.convertPedidosRawValueToPedidos(form.getRawValue() as PedidosFormRawValue | NewPedidosFormRawValue);
  }

  resetForm(form: PedidosFormGroup, pedidos: PedidosFormGroupInput): void {
    const pedidosRawValue = this.convertPedidosToPedidosRawValue({ ...this.getFormDefaults(), ...pedidos });
    form.reset(
      {
        ...pedidosRawValue,
        id: { value: pedidosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PedidosFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaAlta: currentTime,
      fechaEntrega: currentTime,
    };
  }

  private convertPedidosRawValueToPedidos(rawPedidos: PedidosFormRawValue | NewPedidosFormRawValue): IPedidos | NewPedidos {
    return {
      ...rawPedidos,
      fechaAlta: dayjs(rawPedidos.fechaAlta, DATE_TIME_FORMAT),
      fechaEntrega: dayjs(rawPedidos.fechaEntrega, DATE_TIME_FORMAT),
    };
  }

  private convertPedidosToPedidosRawValue(
    pedidos: IPedidos | (Partial<NewPedidos> & PedidosFormDefaults),
  ): PedidosFormRawValue | PartialWithRequiredKeyOf<NewPedidosFormRawValue> {
    return {
      ...pedidos,
      fechaAlta: pedidos.fechaAlta ? pedidos.fechaAlta.format(DATE_TIME_FORMAT) : undefined,
      fechaEntrega: pedidos.fechaEntrega ? pedidos.fechaEntrega.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
