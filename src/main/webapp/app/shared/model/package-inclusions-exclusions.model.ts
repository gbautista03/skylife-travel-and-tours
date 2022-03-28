export interface IPackageInclusionsExclusions {
  id?: number;
  destination?: string;
  inclusions?: string;
  exclusions?: string;
}

export const defaultValue: Readonly<IPackageInclusionsExclusions> = {};
