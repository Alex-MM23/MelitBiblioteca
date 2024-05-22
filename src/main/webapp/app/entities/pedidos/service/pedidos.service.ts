import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPedidos, NewPedidos } from '../pedidos.model';

export type PartialUpdatePedidos = Partial<IPedidos> & Pick<IPedidos, 'id'>;

type RestOf<T extends IPedidos | NewPedidos> = Omit<T, 'fechaAlta' | 'fechaEntrega'> & {
  fechaAlta?: string | null;
  fechaEntrega?: string | null;
};

export type RestPedidos = RestOf<IPedidos>;

export type NewRestPedidos = RestOf<NewPedidos>;

export type PartialUpdateRestPedidos = RestOf<PartialUpdatePedidos>;

export type EntityResponseType = HttpResponse<IPedidos>;
export type EntityArrayResponseType = HttpResponse<IPedidos[]>;

@Injectable({ providedIn: 'root' })
export class PedidosService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pedidos');

  create(pedidos: NewPedidos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pedidos);
    return this.http
      .post<RestPedidos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pedidos: IPedidos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pedidos);
    return this.http
      .put<RestPedidos>(`${this.resourceUrl}/${this.getPedidosIdentifier(pedidos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pedidos: PartialUpdatePedidos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pedidos);
    return this.http
      .patch<RestPedidos>(`${this.resourceUrl}/${this.getPedidosIdentifier(pedidos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPedidos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPedidos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPedidosIdentifier(pedidos: Pick<IPedidos, 'id'>): number {
    return pedidos.id;
  }

  comparePedidos(o1: Pick<IPedidos, 'id'> | null, o2: Pick<IPedidos, 'id'> | null): boolean {
    return o1 && o2 ? this.getPedidosIdentifier(o1) === this.getPedidosIdentifier(o2) : o1 === o2;
  }

  addPedidosToCollectionIfMissing<Type extends Pick<IPedidos, 'id'>>(
    pedidosCollection: Type[],
    ...pedidosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pedidos: Type[] = pedidosToCheck.filter(isPresent);
    if (pedidos.length > 0) {
      const pedidosCollectionIdentifiers = pedidosCollection.map(pedidosItem => this.getPedidosIdentifier(pedidosItem));
      const pedidosToAdd = pedidos.filter(pedidosItem => {
        const pedidosIdentifier = this.getPedidosIdentifier(pedidosItem);
        if (pedidosCollectionIdentifiers.includes(pedidosIdentifier)) {
          return false;
        }
        pedidosCollectionIdentifiers.push(pedidosIdentifier);
        return true;
      });
      return [...pedidosToAdd, ...pedidosCollection];
    }
    return pedidosCollection;
  }

  protected convertDateFromClient<T extends IPedidos | NewPedidos | PartialUpdatePedidos>(pedidos: T): RestOf<T> {
    return {
      ...pedidos,
      fechaAlta: pedidos.fechaAlta?.toJSON() ?? null,
      fechaEntrega: pedidos.fechaEntrega?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPedidos: RestPedidos): IPedidos {
    return {
      ...restPedidos,
      fechaAlta: restPedidos.fechaAlta ? dayjs(restPedidos.fechaAlta) : undefined,
      fechaEntrega: restPedidos.fechaEntrega ? dayjs(restPedidos.fechaEntrega) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPedidos>): HttpResponse<IPedidos> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPedidos[]>): HttpResponse<IPedidos[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
