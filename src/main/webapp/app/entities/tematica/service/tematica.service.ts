import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITematica, NewTematica } from '../tematica.model';

export type PartialUpdateTematica = Partial<ITematica> & Pick<ITematica, 'id'>;

export type EntityResponseType = HttpResponse<ITematica>;
export type EntityArrayResponseType = HttpResponse<ITematica[]>;

@Injectable({ providedIn: 'root' })
export class TematicaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tematicas');

  create(tematica: NewTematica): Observable<EntityResponseType> {
    return this.http.post<ITematica>(this.resourceUrl, tematica, { observe: 'response' });
  }

  update(tematica: ITematica): Observable<EntityResponseType> {
    return this.http.put<ITematica>(`${this.resourceUrl}/${this.getTematicaIdentifier(tematica)}`, tematica, { observe: 'response' });
  }

  partialUpdate(tematica: PartialUpdateTematica): Observable<EntityResponseType> {
    return this.http.patch<ITematica>(`${this.resourceUrl}/${this.getTematicaIdentifier(tematica)}`, tematica, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITematica>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITematica[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTematicaIdentifier(tematica: Pick<ITematica, 'id'>): number {
    return tematica.id;
  }

  compareTematica(o1: Pick<ITematica, 'id'> | null, o2: Pick<ITematica, 'id'> | null): boolean {
    return o1 && o2 ? this.getTematicaIdentifier(o1) === this.getTematicaIdentifier(o2) : o1 === o2;
  }

  addTematicaToCollectionIfMissing<Type extends Pick<ITematica, 'id'>>(
    tematicaCollection: Type[],
    ...tematicasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tematicas: Type[] = tematicasToCheck.filter(isPresent);
    if (tematicas.length > 0) {
      const tematicaCollectionIdentifiers = tematicaCollection.map(tematicaItem => this.getTematicaIdentifier(tematicaItem));
      const tematicasToAdd = tematicas.filter(tematicaItem => {
        const tematicaIdentifier = this.getTematicaIdentifier(tematicaItem);
        if (tematicaCollectionIdentifiers.includes(tematicaIdentifier)) {
          return false;
        }
        tematicaCollectionIdentifiers.push(tematicaIdentifier);
        return true;
      });
      return [...tematicasToAdd, ...tematicaCollection];
    }
    return tematicaCollection;
  }
}
