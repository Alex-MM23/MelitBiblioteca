import { Component } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';

@Component({
  selector: 'jhi-buscador-admin',
  standalone: true,
  imports: [],
  templateUrl: './buscador-admin.component.html',
  styleUrl: './buscador-admin.component.scss',
})
export class BuscadorAdminComponent {
  @Output() TextoFiltro = new EventEmitter<string>();

  onKeysUp(event: KeyboardEvent) {
    const texto = (event.target as HTMLInputElement).value;
    this.TextoFiltro.emit(texto);
  }
}
