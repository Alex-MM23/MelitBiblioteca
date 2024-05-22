import { ILibros, NewLibros } from './libros.model';

export const sampleWithRequiredData: ILibros = {
  id: 21843,
};

export const sampleWithPartialData: ILibros = {
  id: 11927,
  stock: 19801,
  imagen: 'minty',
  numeroAlquilados: 19440,
  idTematica: 1788,
};

export const sampleWithFullData: ILibros = {
  id: 15673,
  isbn: 16352,
  stock: 6187,
  autor: 'nor telecommute pain',
  imagen: 'light although saw',
  paginas: 4511,
  titulo: 'because',
  numeroAlquilados: 38,
  idTematica: 15679,
};

export const sampleWithNewData: NewLibros = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
