import dayjs from 'dayjs/esm';

import { IUsuarios, NewUsuarios } from './usuarios.model';

export const sampleWithRequiredData: IUsuarios = {
  id: 12526,
};

export const sampleWithPartialData: IUsuarios = {
  id: 12263,
  username: 'hurry',
  password: 'huff scenario hence',
  email: 'Carlota.AlmonteFuentes43@yahoo.com',
  nombre: 'private spectacular apron',
  fechaAlta: dayjs('2024-05-21T21:43'),
};

export const sampleWithFullData: IUsuarios = {
  id: 15699,
  username: 'fraudster aha less',
  password: 'specialist jeweller separately',
  email: 'Fernando.ContrerasLlamas@hotmail.com',
  nombre: 'even pace',
  apellido: 'barring yearningly hm',
  direccion: 'finally relative boil',
  fechaAlta: dayjs('2024-05-21T12:58'),
};

export const sampleWithNewData: NewUsuarios = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
