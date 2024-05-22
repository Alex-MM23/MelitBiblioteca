import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UsuariosDetailComponent } from './usuarios-detail.component';

describe('Usuarios Management Detail Component', () => {
  let comp: UsuariosDetailComponent;
  let fixture: ComponentFixture<UsuariosDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsuariosDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UsuariosDetailComponent,
              resolve: { usuarios: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UsuariosDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UsuariosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load usuarios on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UsuariosDetailComponent);

      // THEN
      expect(instance.usuarios()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
