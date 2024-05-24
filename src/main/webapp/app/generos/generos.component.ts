import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-generos',
  standalone: true,
  imports: [SharedModule, RouterModule],
  templateUrl: './generos.component.html',
  styleUrl: './generos.component.scss',
})
export class GenerosComponent {
  private router = inject(Router);
  algo(): void {}

  ruta(): void {
    this.router.navigate(['Libros']);
  }
}
