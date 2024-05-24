import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PedidosComponent } from './list/pedidos.component';
import { PedidosDetailComponent } from './detail/pedidos-detail.component';
import { PedidosUpdateComponent } from './update/pedidos-update.component';
import PedidosResolve from './route/pedidos-routing-resolve.service';

const pedidosRoute: Routes = [
  {
    path: '',
    component: PedidosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PedidosDetailComponent,
    resolve: {
      pedidos: PedidosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PedidosUpdateComponent,
    resolve: {
      pedidos: PedidosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PedidosUpdateComponent,
    resolve: {
      pedidos: PedidosResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pedidosRoute;
