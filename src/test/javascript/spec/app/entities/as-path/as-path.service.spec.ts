import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ASPathService } from 'app/entities/as-path/as-path.service';
import { IASPath, ASPath } from 'app/shared/model/as-path.model';
import { PATHTYPE } from 'app/shared/model/enumerations/pathtype.model';
import { EventStatus } from 'app/shared/model/enumerations/event-status.model';

describe('Service Tests', () => {
  describe('ASPath Service', () => {
    let injector: TestBed;
    let service: ASPathService;
    let httpMock: HttpTestingController;
    let elemDefault: IASPath;
    let expectedResult: IASPath | IASPath[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ASPathService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new ASPath(
        0,
        PATHTYPE.AKHAND_PATH,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        EventStatus.BOOKED,
        currentDate,
        'AAAAAAA',
        currentDate,
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            bookingDate: currentDate.format(DATE_TIME_FORMAT),
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

      it('should create a ASPath', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            bookingDate: currentDate.format(DATE_TIME_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            bookingDate: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ASPath()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ASPath', () => {
        const returnedFromService = Object.assign(
          {
            program: 'BBBBBB',
            family: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            address: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            remark: 'BBBBBB',
            bookingDate: currentDate.format(DATE_TIME_FORMAT),
            desc: 'BBBBBB',
            status: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            bookingDate: currentDate,
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

      it('should return a list of ASPath', () => {
        const returnedFromService = Object.assign(
          {
            program: 'BBBBBB',
            family: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            address: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            remark: 'BBBBBB',
            bookingDate: currentDate.format(DATE_TIME_FORMAT),
            desc: 'BBBBBB',
            status: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            createdBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            bookingDate: currentDate,
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

      it('should delete a ASPath', () => {
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
