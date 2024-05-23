import { Component, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { InicioComponent } from 'app/inicio/inicio.component';
import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ModalService } from 'app/services/modal.service';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule, InicioComponent],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account = signal<Account | null>(null);
  modalSwitch: boolean = false;

  private readonly destroy$ = new Subject<void>();

  private accountService = inject(AccountService);
  private router = inject(Router);

  constructor(private modalSS: ModalService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => this.account.set(account));

    this.modalSS.$modal.subscribe(valor => (this.modalSwitch = valor));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  inicio(): void {
    this.router.navigate(['/inicio']);
  }

  generos(): void {
    this.router.navigate(['/generos']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  openModal() {
    this.modalSwitch = true;
  }
}
