import { Moment } from 'moment';

export interface ITask {
  id?: number;
  title?: string;
  description?: string;
  taskTime?: Moment;
}

export class Task implements ITask {
  constructor(public id?: number, public title?: string, public description?: string, public taskTime?: Moment) {}
}
