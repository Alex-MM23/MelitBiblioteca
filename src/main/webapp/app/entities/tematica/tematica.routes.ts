import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TematicaComponent } from './list/tematica.component';
import { TematicaDetailComponent } from './detail/tematica-detail.component';
import { TematicaUpdateComponent } from './update/tematica-update.component';
import TematicaResolve from './route/tematica-routing-resolve.service';

const tematicaRoute: Routes = [
  {
    path: '',
    component: TematicaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TematicaDetailComponent,
    resolve: {
      tematica: TematicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TematicaUpdateComponent,
    resolve: {
      tematica: TematicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TematicaUpdateComponent,
    resolve: {
      tematica: TematicaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tematicaRoute;
