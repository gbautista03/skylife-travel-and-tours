import dayjs from 'dayjs';
import { IPackageTour } from 'app/shared/model/package-tour.model';

export interface IPassenger {
  id?: number;
  firstName?: string;
  lastName?: string;
  birthday?: string;
  gender?: string;
  citizenship?: string;
  contactNumber?: string;
  emailAddress?: string;
  packageTours?: IPackageTour[] | null;
}

export const defaultValue: Readonly<IPassenger> = {};
