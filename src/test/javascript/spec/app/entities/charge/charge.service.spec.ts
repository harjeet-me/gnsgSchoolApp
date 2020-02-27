import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { ChargeService } from 'app/entities/charge/charge.service';
import { ICharge, Charge } from 'app/shared/model/charge.model';

describe('Service Tests', () => {
  describe('Charge Service', () => {
    let injector: TestBed;
    let service: ChargeService;
    let httpMock: HttpTestingController;
    let elemDefault: ICharge;
    let expectedResult: ICharge | ICharge[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ChargeService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Charge(0, 'AAAAAAA', 0, currentDate, currentDate, 0, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dueDate: currentDate.format(DATE_FORMAT),
            paymentDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Charge', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dueDate: currentDate.format(DATE_FORMAT),
            paymentDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate,
            paymentDate: currentDate
          },
          returnedFromService
        );

        service.create(new Charge()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Charge', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            ammount: 1,
            dueDate: currentDate.format(DATE_FORMAT),
            paymentDate: currentDate.format(DATE_FORMAT),
            ammountPaid: 1,
            ref: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate,
            paymentDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Charge', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            ammount: 1,
            dueDate: currentDate.format(DATE_FORMAT),
            paymentDate: currentDate.format(DATE_FORMAT),
            ammountPaid: 1,
            ref: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dueDate: currentDate,
            paymentDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Charge', () => {
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
