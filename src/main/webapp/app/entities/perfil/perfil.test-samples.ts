import { IPerfil, NewPerfil } from './perfil.model';

export const sampleWithRequiredData: IPerfil = {
  id: 28235,
};

export const sampleWithPartialData: IPerfil = {
  id: 448,
  usuario: 'until',
};

export const sampleWithFullData: IPerfil = {
  id: 25857,
  admin: 'dunk clean save',
  usuario: 'huzzah voluntarily than',
};

export const sampleWithNewData: NewPerfil = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
