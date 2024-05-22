import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUsuarios } from '../usuarios.model';
import { UsuariosService } from '../service/usuarios.service';
import { UsuariosFormService, UsuariosFormGroup } from './usuarios-form.service';

@Component({
  standalone: true,
  selector: 'jhi-usuarios-update',
  templateUrl: './usuarios-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UsuariosUpdateComponent implements OnInit {
  isSaving = false;
  usuarios: IUsuarios | null = null;

  protected usuariosService = inject(UsuariosService);
  protected usuariosFormService = inject(UsuariosFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UsuariosFormGroup = this.usuariosFormService.createUsuariosFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarios }) => {
      this.usuarios = usuarios;
      if (usuarios) {
        this.updateForm(usuarios);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuarios = this.usuariosFormService.getUsuarios(this.editForm);
    if (usuarios.id !== null) {
      this.subscribeToSaveResponse(this.usuariosService.update(usuarios));
    } else {
      this.subscribeToSaveResponse(this.usuariosService.create(usuarios));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuarios>>): void {
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

  protected updateForm(usuarios: IUsuarios): void {
    this.usuarios = usuarios;
    this.usuariosFormService.resetForm(this.editForm, usuarios);
  }
}
