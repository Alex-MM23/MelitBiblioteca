import dayjs from 'dayjs/esm';

import { IPedidos, NewPedidos } from './pedidos.model';

export const sampleWithRequiredData: IPedidos = {
  id: 13881,
};

export const sampleWithPartialData: IPedidos = {
  id: 12327,
  username: 'nor',
};

export const sampleWithFullData: IPedidos = {
  id: 6906,
  direccion: 'shimmer oh when',
  fechaAlta: dayjs('2024-05-23T14:59'),
  fechaEntrega: dayjs('2024-05-22T17:42'),
  username: 'ear',
};

export const sampleWithNewData: NewPedidos = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
