import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '3565a38b-8ab2-433c-a884-a5db6c55754a',
};

export const sampleWithPartialData: IAuthority = {
  name: 'b6925bd8-3b23-4398-802e-23c58bad56a2',
};

export const sampleWithFullData: IAuthority = {
  name: 'fd0afd9b-3b71-4fdf-9beb-833763e4b09e',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
