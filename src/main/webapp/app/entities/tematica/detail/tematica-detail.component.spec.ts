import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TematicaDetailComponent } from './tematica-detail.component';

describe('Tematica Management Detail Component', () => {
  let comp: TematicaDetailComponent;
  let fixture: ComponentFixture<TematicaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TematicaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TematicaDetailComponent,
              resolve: { tematica: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TematicaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TematicaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tematica on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TematicaDetailComponent);

      // THEN
      expect(instance.tematica()).toEqual(expect.objectContaining({ id: 123 }));
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
