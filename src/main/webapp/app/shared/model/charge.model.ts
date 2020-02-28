import { Moment } from 'moment';
import { IStudent } from 'app/shared/model/student.model';
import { ChargeStatus } from 'app/shared/model/enumerations/charge-status.model';

export interface ICharge {
  id?: number;
  name?: string;
  amt?: number;
  month?: string;
  dueDate?: Moment;
  paymentDate?: Moment;
  amtPaid?: number;
  status?: ChargeStatus;
  ref?: string;
  student?: IStudent;
}

export class Charge implements ICharge {
  constructor(
    public id?: number,
    public name?: string,
    public amt?: number,
    public month?: string,
    public dueDate?: Moment,
    public paymentDate?: Moment,
    public amtPaid?: number,
    public status?: ChargeStatus,
    public ref?: string,
    public student?: IStudent
  ) {}
}
