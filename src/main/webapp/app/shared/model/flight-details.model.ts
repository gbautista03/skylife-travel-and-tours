import dayjs from 'dayjs';
import { IPackageTour } from 'app/shared/model/package-tour.model';

export interface IFlightDetails {
  id?: number;
  origin?: string;
  destination?: string;
  flightNumber?: string;
  carrier?: string;
  departureDate?: string;
  arrivalDate?: string | null;
  packageTours?: IPackageTour[] | null;
}

export const defaultValue: Readonly<IFlightDetails> = {};
