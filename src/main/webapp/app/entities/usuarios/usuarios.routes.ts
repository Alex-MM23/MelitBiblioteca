import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UsuariosComponent } from './list/usuarios.component';
import { UsuariosDetailComponent } from './detail/usuarios-detail.component';
import { UsuariosUpdateComponent } from './update/usuarios-update.component';
import UsuariosResolve from './route/usuarios-routing-resolve.service';

const usuariosRoute: Routes = [
  {
    path: '',
    component: UsuariosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UsuariosDetailComponent,
    resolve: {
      usuarios: UsuariosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UsuariosUpdateComponent,
    resolve: {
      usuarios: UsuariosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UsuariosUpdateComponent,
    resolve: {
      usuarios: UsuariosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default usuariosRoute;
