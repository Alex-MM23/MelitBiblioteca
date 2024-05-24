import { ITematica, NewTematica } from './tematica.model';

export const sampleWithRequiredData: ITematica = {
  id: 15335,
};

export const sampleWithPartialData: ITematica = {
  id: 32252,
  descripcion: 'but church',
};

export const sampleWithFullData: ITematica = {
  id: 18918,
  abreviatura: 'of',
  descripcion: 'even',
};

export const sampleWithNewData: NewTematica = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
