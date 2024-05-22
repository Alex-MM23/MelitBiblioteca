import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IUsuarios } from '../usuarios.model';

@Component({
  standalone: true,
  selector: 'jhi-usuarios-detail',
  templateUrl: './usuarios-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UsuariosDetailComponent {
  usuarios = input<IUsuarios | null>(null);

  previousState(): void {
    window.history.back();
  }
}
