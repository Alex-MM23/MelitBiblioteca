import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUsuarios, NewUsuarios } from '../usuarios.model';

export type PartialUpdateUsuarios = Partial<IUsuarios> & Pick<IUsuarios, 'id'>;

type RestOf<T extends IUsuarios | NewUsuarios> = Omit<T, 'fechaAlta'> & {
  fechaAlta?: string | null;
};

export type RestUsuarios = RestOf<IUsuarios>;

export type NewRestUsuarios = RestOf<NewUsuarios>;

export type PartialUpdateRestUsuarios = RestOf<PartialUpdateUsuarios>;

export type EntityResponseType = HttpResponse<IUsuarios>;
export type EntityArrayResponseType = HttpResponse<IUsuarios[]>;

@Injectable({ providedIn: 'root' })
export class UsuariosService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/usuarios');

  create(usuarios: NewUsuarios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuarios);
    return this.http
      .post<RestUsuarios>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(usuarios: IUsuarios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuarios);
    return this.http
      .put<RestUsuarios>(`${this.resourceUrl}/${this.getUsuariosIdentifier(usuarios)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(usuarios: PartialUpdateUsuarios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuarios);
    return this.http
      .patch<RestUsuarios>(`${this.resourceUrl}/${this.getUsuariosIdentifier(usuarios)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUsuarios>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUsuarios[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUsuariosIdentifier(usuarios: Pick<IUsuarios, 'id'>): number {
    return usuarios.id;
  }

  compareUsuarios(o1: Pick<IUsuarios, 'id'> | null, o2: Pick<IUsuarios, 'id'> | null): boolean {
    return o1 && o2 ? this.getUsuariosIdentifier(o1) === this.getUsuariosIdentifier(o2) : o1 === o2;
  }

  addUsuariosToCollectionIfMissing<Type extends Pick<IUsuarios, 'id'>>(
    usuariosCollection: Type[],
    ...usuariosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const usuarios: Type[] = usuariosToCheck.filter(isPresent);
    if (usuarios.length > 0) {
      const usuariosCollectionIdentifiers = usuariosCollection.map(usuariosItem => this.getUsuariosIdentifier(usuariosItem));
      const usuariosToAdd = usuarios.filter(usuariosItem => {
        const usuariosIdentifier = this.getUsuariosIdentifier(usuariosItem);
        if (usuariosCollectionIdentifiers.includes(usuariosIdentifier)) {
          return false;
        }
        usuariosCollectionIdentifiers.push(usuariosIdentifier);
        return true;
      });
      return [...usuariosToAdd, ...usuariosCollection];
    }
    return usuariosCollection;
  }

  protected convertDateFromClient<T extends IUsuarios | NewUsuarios | PartialUpdateUsuarios>(usuarios: T): RestOf<T> {
    return {
      ...usuarios,
      fechaAlta: usuarios.fechaAlta?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUsuarios: RestUsuarios): IUsuarios {
    return {
      ...restUsuarios,
      fechaAlta: restUsuarios.fechaAlta ? dayjs(restUsuarios.fechaAlta) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUsuarios>): HttpResponse<IUsuarios> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUsuarios[]>): HttpResponse<IUsuarios[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
