import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ILibros } from '../libros.model';

@Component({
  standalone: true,
  selector: 'jhi-libros-detail',
  templateUrl: './libros-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class LibrosDetailComponent {
  libros = input<ILibros | null>(null);

  previousState(): void {
    window.history.back();
  }
}
