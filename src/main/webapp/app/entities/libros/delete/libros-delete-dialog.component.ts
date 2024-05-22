import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILibros } from '../libros.model';
import { LibrosService } from '../service/libros.service';

@Component({
  standalone: true,
  templateUrl: './libros-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LibrosDeleteDialogComponent {
  libros?: ILibros;

  protected librosService = inject(LibrosService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.librosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
