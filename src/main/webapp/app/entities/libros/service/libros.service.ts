import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILibros, NewLibros } from '../libros.model';

export type PartialUpdateLibros = Partial<ILibros> & Pick<ILibros, 'id'>;

export type EntityResponseType = HttpResponse<ILibros>;
export type EntityArrayResponseType = HttpResponse<ILibros[]>;

@Injectable({ providedIn: 'root' })
export class LibrosService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/libros');

  create(libros: NewLibros): Observable<EntityResponseType> {
    return this.http.post<ILibros>(this.resourceUrl, libros, { observe: 'response' });
  }

  update(libros: ILibros): Observable<EntityResponseType> {
    return this.http.put<ILibros>(`${this.resourceUrl}/${this.getLibrosIdentifier(libros)}`, libros, { observe: 'response' });
  }

  partialUpdate(libros: PartialUpdateLibros): Observable<EntityResponseType> {
    return this.http.patch<ILibros>(`${this.resourceUrl}/${this.getLibrosIdentifier(libros)}`, libros, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILibros>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILibros[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLibrosIdentifier(libros: Pick<ILibros, 'id'>): number {
    return libros.id;
  }

  compareLibros(o1: Pick<ILibros, 'id'> | null, o2: Pick<ILibros, 'id'> | null): boolean {
    return o1 && o2 ? this.getLibrosIdentifier(o1) === this.getLibrosIdentifier(o2) : o1 === o2;
  }

  addLibrosToCollectionIfMissing<Type extends Pick<ILibros, 'id'>>(
    librosCollection: Type[],
    ...librosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const libros: Type[] = librosToCheck.filter(isPresent);
    if (libros.length > 0) {
      const librosCollectionIdentifiers = librosCollection.map(librosItem => this.getLibrosIdentifier(librosItem));
      const librosToAdd = libros.filter(librosItem => {
        const librosIdentifier = this.getLibrosIdentifier(librosItem);
        if (librosCollectionIdentifiers.includes(librosIdentifier)) {
          return false;
        }
        librosCollectionIdentifiers.push(librosIdentifier);
        return true;
      });
      return [...librosToAdd, ...librosCollection];
    }
    return librosCollection;
  }
}
