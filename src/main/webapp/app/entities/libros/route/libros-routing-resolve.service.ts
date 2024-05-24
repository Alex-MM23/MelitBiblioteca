import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILibros } from '../libros.model';
import { LibrosService } from '../service/libros.service';

const librosResolve = (route: ActivatedRouteSnapshot): Observable<null | ILibros> => {
  const id = route.params['id'];
  if (id) {
    return inject(LibrosService)
      .find(id)
      .pipe(
        mergeMap((libros: HttpResponse<ILibros>) => {
          if (libros.body) {
            return of(libros.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default librosResolve;
