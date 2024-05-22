import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUsuarios } from '../usuarios.model';
import { UsuariosService } from '../service/usuarios.service';

const usuariosResolve = (route: ActivatedRouteSnapshot): Observable<null | IUsuarios> => {
  const id = route.params['id'];
  if (id) {
    return inject(UsuariosService)
      .find(id)
      .pipe(
        mergeMap((usuarios: HttpResponse<IUsuarios>) => {
          if (usuarios.body) {
            return of(usuarios.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default usuariosResolve;
