export interface IOHDC {
  id?: number;
  destination?: string;
  description?: string;
  contactDescription?: string;
}

export const defaultValue: Readonly<IOHDC> = {};
