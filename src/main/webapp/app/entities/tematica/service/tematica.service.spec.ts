import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITematica } from '../tematica.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tematica.test-samples';

import { TematicaService } from './tematica.service';

const requireRestSample: ITematica = {
  ...sampleWithRequiredData,
};

describe('Tematica Service', () => {
  let service: TematicaService;
  let httpMock: HttpTestingController;
  let expectedResult: ITematica | ITematica[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TematicaService);
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

    it('should create a Tematica', () => {
      const tematica = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tematica).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tematica', () => {
      const tematica = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tematica).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tematica', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tematica', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Tematica', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTematicaToCollectionIfMissing', () => {
      it('should add a Tematica to an empty array', () => {
        const tematica: ITematica = sampleWithRequiredData;
        expectedResult = service.addTematicaToCollectionIfMissing([], tematica);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tematica);
      });

      it('should not add a Tematica to an array that contains it', () => {
        const tematica: ITematica = sampleWithRequiredData;
        const tematicaCollection: ITematica[] = [
          {
            ...tematica,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTematicaToCollectionIfMissing(tematicaCollection, tematica);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tematica to an array that doesn't contain it", () => {
        const tematica: ITematica = sampleWithRequiredData;
        const tematicaCollection: ITematica[] = [sampleWithPartialData];
        expectedResult = service.addTematicaToCollectionIfMissing(tematicaCollection, tematica);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tematica);
      });

      it('should add only unique Tematica to an array', () => {
        const tematicaArray: ITematica[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tematicaCollection: ITematica[] = [sampleWithRequiredData];
        expectedResult = service.addTematicaToCollectionIfMissing(tematicaCollection, ...tematicaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tematica: ITematica = sampleWithRequiredData;
        const tematica2: ITematica = sampleWithPartialData;
        expectedResult = service.addTematicaToCollectionIfMissing([], tematica, tematica2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tematica);
        expect(expectedResult).toContain(tematica2);
      });

      it('should accept null and undefined values', () => {
        const tematica: ITematica = sampleWithRequiredData;
        expectedResult = service.addTematicaToCollectionIfMissing([], null, tematica, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tematica);
      });

      it('should return initial array if no Tematica is added', () => {
        const tematicaCollection: ITematica[] = [sampleWithRequiredData];
        expectedResult = service.addTematicaToCollectionIfMissing(tematicaCollection, undefined, null);
        expect(expectedResult).toEqual(tematicaCollection);
      });
    });

    describe('compareTematica', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTematica(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTematica(entity1, entity2);
        const compareResult2 = service.compareTematica(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTematica(entity1, entity2);
        const compareResult2 = service.compareTematica(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTematica(entity1, entity2);
        const compareResult2 = service.compareTematica(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
