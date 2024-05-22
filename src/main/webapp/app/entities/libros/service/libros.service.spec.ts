import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILibros } from '../libros.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../libros.test-samples';

import { LibrosService } from './libros.service';

const requireRestSample: ILibros = {
  ...sampleWithRequiredData,
};

describe('Libros Service', () => {
  let service: LibrosService;
  let httpMock: HttpTestingController;
  let expectedResult: ILibros | ILibros[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LibrosService);
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

    it('should create a Libros', () => {
      const libros = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(libros).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Libros', () => {
      const libros = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(libros).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Libros', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Libros', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Libros', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLibrosToCollectionIfMissing', () => {
      it('should add a Libros to an empty array', () => {
        const libros: ILibros = sampleWithRequiredData;
        expectedResult = service.addLibrosToCollectionIfMissing([], libros);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(libros);
      });

      it('should not add a Libros to an array that contains it', () => {
        const libros: ILibros = sampleWithRequiredData;
        const librosCollection: ILibros[] = [
          {
            ...libros,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLibrosToCollectionIfMissing(librosCollection, libros);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Libros to an array that doesn't contain it", () => {
        const libros: ILibros = sampleWithRequiredData;
        const librosCollection: ILibros[] = [sampleWithPartialData];
        expectedResult = service.addLibrosToCollectionIfMissing(librosCollection, libros);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(libros);
      });

      it('should add only unique Libros to an array', () => {
        const librosArray: ILibros[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const librosCollection: ILibros[] = [sampleWithRequiredData];
        expectedResult = service.addLibrosToCollectionIfMissing(librosCollection, ...librosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const libros: ILibros = sampleWithRequiredData;
        const libros2: ILibros = sampleWithPartialData;
        expectedResult = service.addLibrosToCollectionIfMissing([], libros, libros2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(libros);
        expect(expectedResult).toContain(libros2);
      });

      it('should accept null and undefined values', () => {
        const libros: ILibros = sampleWithRequiredData;
        expectedResult = service.addLibrosToCollectionIfMissing([], null, libros, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(libros);
      });

      it('should return initial array if no Libros is added', () => {
        const librosCollection: ILibros[] = [sampleWithRequiredData];
        expectedResult = service.addLibrosToCollectionIfMissing(librosCollection, undefined, null);
        expect(expectedResult).toEqual(librosCollection);
      });
    });

    describe('compareLibros', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLibros(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLibros(entity1, entity2);
        const compareResult2 = service.compareLibros(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLibros(entity1, entity2);
        const compareResult2 = service.compareLibros(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLibros(entity1, entity2);
        const compareResult2 = service.compareLibros(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
