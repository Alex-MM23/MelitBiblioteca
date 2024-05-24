import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'libros',
    data: { pageTitle: 'Libros' },
    loadChildren: () => import('./libros/libros.routes'),
  },
  {
    path: 'tematica',
    data: { pageTitle: 'Tematicas' },
    loadChildren: () => import('./tematica/tematica.routes'),
  },
  {
    path: 'pedidos',
    data: { pageTitle: 'Pedidos' },
    loadChildren: () => import('./pedidos/pedidos.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
