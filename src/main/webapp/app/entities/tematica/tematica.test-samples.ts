import { ITematica, NewTematica } from './tematica.model';

export const sampleWithRequiredData: ITematica = {
  id: 15542,
};

export const sampleWithPartialData: ITematica = {
  id: 17022,
  abreviatura: 'everyone whelp',
  descripcion: 'ninja athletics',
};

export const sampleWithFullData: ITematica = {
  id: 856,
  abreviatura: 'daily',
  descripcion: 'unrealistic till until',
};

export const sampleWithNewData: NewTematica = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
