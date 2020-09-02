import { Moment } from 'moment';
import { IPRoul } from 'app/shared/model/p-roul.model';
import { PATHTYPE } from 'app/shared/model/enumerations/pathtype.model';
import { EventStatus } from 'app/shared/model/enumerations/event-status.model';

export interface IASPath {
  id?: number;
  program?: PATHTYPE;
  family?: string;
  phoneNumber?: string;
  address?: string;
  startDate?: Moment;
  endDate?: Moment;
  remark?: string;
  bookingDate?: Moment;
  desc?: string;
  status?: EventStatus;
  createdDate?: Moment;
  createdBy?: string;
  lastModifiedDate?: Moment;
  lastModifiedBy?: string;
  pRouls?: IPRoul[];
}

export class ASPath implements IASPath {
  constructor(
    public id?: number,
    public program?: PATHTYPE,
    public family?: string,
    public phoneNumber?: string,
    public address?: string,
    public startDate?: Moment,
    public endDate?: Moment,
    public remark?: string,
    public bookingDate?: Moment,
    public desc?: string,
    public status?: EventStatus,
    public createdDate?: Moment,
    public createdBy?: string,
    public lastModifiedDate?: Moment,
    public lastModifiedBy?: string,
    public pRouls?: IPRoul[]
  ) {}
}
