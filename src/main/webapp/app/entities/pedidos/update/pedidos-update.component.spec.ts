import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { PedidosService } from '../service/pedidos.service';
import { IPedidos } from '../pedidos.model';
import { PedidosFormService } from './pedidos-form.service';

import { PedidosUpdateComponent } from './pedidos-update.component';

describe('Pedidos Management Update Component', () => {
  let comp: PedidosUpdateComponent;
  let fixture: ComponentFixture<PedidosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pedidosFormService: PedidosFormService;
  let pedidosService: PedidosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PedidosUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PedidosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PedidosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pedidosFormService = TestBed.inject(PedidosFormService);
    pedidosService = TestBed.inject(PedidosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pedidos: IPedidos = { id: 456 };

      activatedRoute.data = of({ pedidos });
      comp.ngOnInit();

      expect(comp.pedidos).toEqual(pedidos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPedidos>>();
      const pedidos = { id: 123 };
      jest.spyOn(pedidosFormService, 'getPedidos').mockReturnValue(pedidos);
      jest.spyOn(pedidosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pedidos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pedidos }));
      saveSubject.complete();

      // THEN
      expect(pedidosFormService.getPedidos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pedidosService.update).toHaveBeenCalledWith(expect.objectContaining(pedidos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPedidos>>();
      const pedidos = { id: 123 };
      jest.spyOn(pedidosFormService, 'getPedidos').mockReturnValue({ id: null });
      jest.spyOn(pedidosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pedidos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pedidos }));
      saveSubject.complete();

      // THEN
      expect(pedidosFormService.getPedidos).toHaveBeenCalled();
      expect(pedidosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPedidos>>();
      const pedidos = { id: 123 };
      jest.spyOn(pedidosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pedidos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pedidosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
