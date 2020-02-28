import { ICharge } from 'app/shared/model/charge.model';
import { IAppliedCharge } from 'app/shared/model/applied-charge.model';

export interface IStudent {
  id?: number;
  fullName?: string;
  parentName?: string;
  address?: string;
  city?: string;
  stateProvince?: string;
  postalCode?: string;
  email?: string;
  phoneNumber?: number;
  teacherName?: string;
  active?: boolean;
  charges?: ICharge[];
  appliedCharges?: IAppliedCharge[];
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public fullName?: string,
    public parentName?: string,
    public address?: string,
    public city?: string,
    public stateProvince?: string,
    public postalCode?: string,
    public email?: string,
    public phoneNumber?: number,
    public teacherName?: string,
    public active?: boolean,
    public charges?: ICharge[],
    public appliedCharges?: IAppliedCharge[]
  ) {
    this.active = this.active || false;
  }
}
