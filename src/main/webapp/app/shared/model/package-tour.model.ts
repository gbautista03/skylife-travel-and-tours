import dayjs from 'dayjs';
import { IPackageInclusionsExclusions } from 'app/shared/model/package-inclusions-exclusions.model';
import { IRequirements } from 'app/shared/model/requirements.model';
import { IOHDC } from 'app/shared/model/ohdc.model';
import { IPassenger } from 'app/shared/model/passenger.model';
import { IFlightDetails } from 'app/shared/model/flight-details.model';

export interface IPackageTour {
  id?: number;
  days?: number;
  nights?: number;
  destination?: string;
  tourCode?: string;
  date?: string;
  hotel?: string;
  roomType?: string;
  numberOfGuest?: number;
  inclusionExclusion?: IPackageInclusionsExclusions | null;
  requirements?: IRequirements | null;
  ohdc?: IOHDC | null;
  passengers?: IPassenger[] | null;
  flightDetails?: IFlightDetails[] | null;
}

export const defaultValue: Readonly<IPackageTour> = {};
