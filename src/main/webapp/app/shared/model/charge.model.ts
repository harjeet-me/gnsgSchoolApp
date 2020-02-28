import { Moment } from 'moment';
import { IStudent } from 'app/shared/model/student.model';

export interface ICharge {
  id?: number;
  name?: string;
  ammount?: number;
  month?: string;
  dueDate?: Moment;
  paymentDate?: Moment;
  ammountPaid?: number;
  ref?: string;
  student?: IStudent;
}

export class Charge implements ICharge {
  constructor(
    public id?: number,
    public name?: string,
    public ammount?: number,
    public month?: string,
    public dueDate?: Moment,
    public paymentDate?: Moment,
    public ammountPaid?: number,
    public ref?: string,
    public student?: IStudent
  ) {}
}
