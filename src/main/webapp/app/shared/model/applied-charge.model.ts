import { IStudent } from 'app/shared/model/student.model';

export interface IAppliedCharge {
  id?: number;
  type?: string;
  ammount?: number;
  dueInterval?: number;
  students?: IStudent[];
}

export class AppliedCharge implements IAppliedCharge {
  constructor(
    public id?: number,
    public type?: string,
    public ammount?: number,
    public dueInterval?: number,
    public students?: IStudent[]
  ) {}
}
