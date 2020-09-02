import { Moment } from 'moment';
import { REVTYPE } from 'app/shared/model/enumerations/revtype.model';

export interface IRevenue {
  id?: number;
  revType?: REVTYPE;
  amt?: number;
  date?: Moment;
  desc?: string;
  createdDate?: Moment;
  createdBy?: string;
  lastModifiedDate?: Moment;
  lastModifiedBy?: string;
}

export class Revenue implements IRevenue {
  constructor(
    public id?: number,
    public revType?: REVTYPE,
    public amt?: number,
    public date?: Moment,
    public desc?: string,
    public createdDate?: Moment,
    public createdBy?: string,
    public lastModifiedDate?: Moment,
    public lastModifiedBy?: string
  ) {}
}
