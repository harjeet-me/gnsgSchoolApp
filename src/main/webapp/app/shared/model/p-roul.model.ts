import { Moment } from 'moment';
import { ISevadar } from 'app/shared/model/sevadar.model';
import { IASPath } from 'app/shared/model/as-path.model';

export interface IPRoul {
  id?: number;
  name?: string;
  desc?: string;
  totalRoul?: string;
  createdDate?: Moment;
  createdBy?: string;
  lastModifiedDate?: Moment;
  lastModifiedBy?: string;
  pathi?: ISevadar;
  sehajPath?: IASPath;
}

export class PRoul implements IPRoul {
  constructor(
    public id?: number,
    public name?: string,
    public desc?: string,
    public totalRoul?: string,
    public createdDate?: Moment,
    public createdBy?: string,
    public lastModifiedDate?: Moment,
    public lastModifiedBy?: string,
    public pathi?: ISevadar,
    public sehajPath?: IASPath
  ) {}
}
