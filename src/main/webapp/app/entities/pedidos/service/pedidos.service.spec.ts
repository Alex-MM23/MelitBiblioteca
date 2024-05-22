import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPedidos } from '../pedidos.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pedidos.test-samples';

import { PedidosService, RestPedidos } from './pedidos.service';

const requireRestSample: RestPedidos = {
  ...sampleWithRequiredData,
  fechaAlta: sampleWithRequiredData.fechaAlta?.toJSON(),
  fechaEntrega: sampleWithRequiredData.fechaEntrega?.toJSON(),
};

describe('Pedidos Service', () => {
  let service: PedidosService;
  let httpMock: HttpTestingController;
  let expectedResult: IPedidos | IPedidos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PedidosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Pedidos', () => {
      const pedidos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pedidos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pedidos', () => {
      const pedidos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pedidos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pedidos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pedidos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pedidos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPedidosToCollectionIfMissing', () => {
      it('should add a Pedidos to an empty array', () => {
        const pedidos: IPedidos = sampleWithRequiredData;
        expectedResult = service.addPedidosToCollectionIfMissing([], pedidos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pedidos);
      });

      it('should not add a Pedidos to an array that contains it', () => {
        const pedidos: IPedidos = sampleWithRequiredData;
        const pedidosCollection: IPedidos[] = [
          {
            ...pedidos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPedidosToCollectionIfMissing(pedidosCollection, pedidos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pedidos to an array that doesn't contain it", () => {
        const pedidos: IPedidos = sampleWithRequiredData;
        const pedidosCollection: IPedidos[] = [sampleWithPartialData];
        expectedResult = service.addPedidosToCollectionIfMissing(pedidosCollection, pedidos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pedidos);
      });

      it('should add only unique Pedidos to an array', () => {
        const pedidosArray: IPedidos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pedidosCollection: IPedidos[] = [sampleWithRequiredData];
        expectedResult = service.addPedidosToCollectionIfMissing(pedidosCollection, ...pedidosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pedidos: IPedidos = sampleWithRequiredData;
        const pedidos2: IPedidos = sampleWithPartialData;
        expectedResult = service.addPedidosToCollectionIfMissing([], pedidos, pedidos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pedidos);
        expect(expectedResult).toContain(pedidos2);
      });

      it('should accept null and undefined values', () => {
        const pedidos: IPedidos = sampleWithRequiredData;
        expectedResult = service.addPedidosToCollectionIfMissing([], null, pedidos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pedidos);
      });

      it('should return initial array if no Pedidos is added', () => {
        const pedidosCollection: IPedidos[] = [sampleWithRequiredData];
        expectedResult = service.addPedidosToCollectionIfMissing(pedidosCollection, undefined, null);
        expect(expectedResult).toEqual(pedidosCollection);
      });
    });

    describe('comparePedidos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePedidos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePedidos(entity1, entity2);
        const compareResult2 = service.comparePedidos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePedidos(entity1, entity2);
        const compareResult2 = service.comparePedidos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePedidos(entity1, entity2);
        const compareResult2 = service.comparePedidos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
