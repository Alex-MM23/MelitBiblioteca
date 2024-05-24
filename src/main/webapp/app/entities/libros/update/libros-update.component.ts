import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ILibros } from '../libros.model';
import { LibrosService } from '../service/libros.service';
import { LibrosFormService, LibrosFormGroup } from './libros-form.service';

@Component({
  standalone: true,
  selector: 'jhi-libros-update',
  templateUrl: './libros-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LibrosUpdateComponent implements OnInit {
  isSaving = false;
  libros: ILibros | null = null;

  protected librosService = inject(LibrosService);
  protected librosFormService = inject(LibrosFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LibrosFormGroup = this.librosFormService.createLibrosFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ libros }) => {
      this.libros = libros;
      if (libros) {
        this.updateForm(libros);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const libros = this.librosFormService.getLibros(this.editForm);
    if (libros.id !== null) {
      this.subscribeToSaveResponse(this.librosService.update(libros));
    } else {
      this.subscribeToSaveResponse(this.librosService.create(libros));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILibros>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(libros: ILibros): void {
    this.libros = libros;
    this.librosFormService.resetForm(this.editForm, libros);
  }
}
