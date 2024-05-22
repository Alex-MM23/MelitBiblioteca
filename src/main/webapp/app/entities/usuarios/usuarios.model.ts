import dayjs from 'dayjs/esm';

export interface IUsuarios {
  id: number;
  username?: string | null;
  password?: string | null;
  email?: string | null;
  nombre?: string | null;
  apellido?: string | null;
  direccion?: string | null;
  fechaAlta?: dayjs.Dayjs | null;
}

export type NewUsuarios = Omit<IUsuarios, 'id'> & { id: null };
