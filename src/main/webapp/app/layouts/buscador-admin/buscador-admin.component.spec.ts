import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuscadorAdminComponent } from './buscador-admin.component';

describe('BuscadorAdminComponent', () => {
  let component: BuscadorAdminComponent;
  let fixture: ComponentFixture<BuscadorAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuscadorAdminComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(BuscadorAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
