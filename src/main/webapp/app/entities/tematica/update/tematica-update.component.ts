import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITematica } from '../tematica.model';
import { TematicaService } from '../service/tematica.service';
import { TematicaFormService, TematicaFormGroup } from './tematica-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tematica-update',
  templateUrl: './tematica-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TematicaUpdateComponent implements OnInit {
  isSaving = false;
  tematica: ITematica | null = null;

  protected tematicaService = inject(TematicaService);
  protected tematicaFormService = inject(TematicaFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TematicaFormGroup = this.tematicaFormService.createTematicaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tematica }) => {
      this.tematica = tematica;
      if (tematica) {
        this.updateForm(tematica);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tematica = this.tematicaFormService.getTematica(this.editForm);
    if (tematica.id !== null) {
      this.subscribeToSaveResponse(this.tematicaService.update(tematica));
    } else {
      this.subscribeToSaveResponse(this.tematicaService.create(tematica));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITematica>>): void {
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

  protected updateForm(tematica: ITematica): void {
    this.tematica = tematica;
    this.tematicaFormService.resetForm(this.editForm, tematica);
  }
}
