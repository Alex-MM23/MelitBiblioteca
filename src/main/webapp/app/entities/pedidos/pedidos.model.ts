import dayjs from 'dayjs/esm';

export interface IPedidos {
  id: number;
  direccion?: string | null;
  fechaAlta?: dayjs.Dayjs | null;
  fechaEntrega?: dayjs.Dayjs | null;
  username?: string | null;
}

export type NewPedidos = Omit<IPedidos, 'id'> & { id: null };
