import dayjs from 'dayjs/esm';

import { IPedidos, NewPedidos } from './pedidos.model';

export const sampleWithRequiredData: IPedidos = {
  id: 22845,
};

export const sampleWithPartialData: IPedidos = {
  id: 14487,
  direccion: 'off before ouch',
  fechaEntrega: dayjs('2024-05-21T09:20'),
};

export const sampleWithFullData: IPedidos = {
  id: 14035,
  direccion: 'across ha',
  fechaAlta: dayjs('2024-05-21T10:19'),
  fechaEntrega: dayjs('2024-05-22T01:20'),
  username: 'anenst jalapeño',
};

export const sampleWithNewData: NewPedidos = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
