import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { LibrosComponent } from './list/libros.component';
import { LibrosDetailComponent } from './detail/libros-detail.component';
import { LibrosUpdateComponent } from './update/libros-update.component';
import LibrosResolve from './route/libros-routing-resolve.service';

const librosRoute: Routes = [
  {
    path: '',
    component: LibrosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LibrosDetailComponent,
    resolve: {
      libros: LibrosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LibrosUpdateComponent,
    resolve: {
      libros: LibrosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LibrosUpdateComponent,
    resolve: {
      libros: LibrosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default librosRoute;
