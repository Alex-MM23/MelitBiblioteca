export interface IPerfil {
  id: number;
  admin?: string | null;
  usuario?: string | null;
}

export type NewPerfil = Omit<IPerfil, 'id'> & { id: null };
