export interface ITematica {
  id: number;
  abreviatura?: string | null;
  descripcion?: string | null;
}

export type NewTematica = Omit<ITematica, 'id'> & { id: null };
