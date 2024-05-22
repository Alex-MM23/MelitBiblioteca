import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITematica } from '../tematica.model';
import { TematicaService } from '../service/tematica.service';

const tematicaResolve = (route: ActivatedRouteSnapshot): Observable<null | ITematica> => {
  const id = route.params['id'];
  if (id) {
    return inject(TematicaService)
      .find(id)
      .pipe(
        mergeMap((tematica: HttpResponse<ITematica>) => {
          if (tematica.body) {
            return of(tematica.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default tematicaResolve;
