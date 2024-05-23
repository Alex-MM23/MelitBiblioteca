import { Component, inject, signal, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import SharedModule from 'app/shared/shared.module';
import { Router, RouterModule } from '@angular/router';
import { ModalService } from 'app/services/modal.service';

@Component({
  selector: 'jhi-inicio',
  standalone: true,
  imports: [SharedModule, RouterModule],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.scss',
})
export class InicioComponent implements OnInit {
  account = inject(AccountService).trackCurrentAccount();

  constructor(private modelSS: ModalService) {}

  ngOnInit(): void {}

  closeModal() {
    this.modelSS.$modal.emit(false);
  }
}
