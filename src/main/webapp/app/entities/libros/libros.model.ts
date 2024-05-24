export interface ILibros {
  id: number;
  isbn?: number | null;
  stock?: number | null;
  autor?: string | null;
  imagen?: string | null;
  paginas?: number | null;
  titulo?: string | null;
  numeroAlquilados?: number | null;
}

export type NewLibros = Omit<ILibros, 'id'> & { id: null };
