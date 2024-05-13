import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 12085,
  login: 'kuW0@E7g\\h7cFB',
};

export const sampleWithPartialData: IUser = {
  id: 21444,
  login: '@',
};

export const sampleWithFullData: IUser = {
  id: 7294,
  login: 'JS5@9tXVCB\\yMOSlO\\CA78\\(OnlwkP',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
