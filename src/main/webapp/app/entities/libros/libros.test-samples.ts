import { ILibros, NewLibros } from './libros.model';

export const sampleWithRequiredData: ILibros = {
  id: 19496,
};

export const sampleWithPartialData: ILibros = {
  id: 15159,
  titulo: 'consequently solidly',
};

export const sampleWithFullData: ILibros = {
  id: 28763,
  isbn: 13792,
  stock: 17138,
  autor: 'kiddingly',
  imagen: 'for yippee',
  paginas: 7099,
  titulo: 'fondly',
  numeroAlquilados: 26539,
};

export const sampleWithNewData: NewLibros = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
