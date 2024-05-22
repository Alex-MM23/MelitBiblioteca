import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPerfil } from '../perfil.model';

@Component({
  standalone: true,
  selector: 'jhi-perfil-detail',
  templateUrl: './perfil-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PerfilDetailComponent {
  perfil = input<IPerfil | null>(null);

  previousState(): void {
    window.history.back();
  }
}
