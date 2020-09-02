import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ExpenseService } from 'app/entities/expense/expense.service';
import { IExpense, Expense } from 'app/shared/model/expense.model';
import { EXPTYPE } from 'app/shared/model/enumerations/exptype.model';

describe('Service Tests', () => {
  describe('Expense Service', () => {
    let injector: TestBed;
    let service: ExpenseService;
    let httpMock: HttpTestingController;
    let elemDefault: IExpense;
    let expectedResult: IExpense | IExpense[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ExpenseService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Expense(0, EXPTYPE.INVOICE, 0, currentDate, 'AAAAAAA', currentDate, 'AAAAAAA', currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Expense', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Expense()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Expense', () => {
        const returnedFromService = Object.assign(
          {
            expType: 'BBBBBB',
            amt: 1,
            date: currentDate.format(DATE_FORMAT),
            desc: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Expense', () => {
        const returnedFromService = Object.assign(
          {
            expType: 'BBBBBB',
            amt: 1,
            date: currentDate.format(DATE_FORMAT),
            desc: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Expense', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
