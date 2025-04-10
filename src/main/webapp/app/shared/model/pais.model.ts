import { IRegion } from 'app/shared/model/region.model';

export interface IPais {
  id?: number;
  nombrePais?: string | null;
  region?: IRegion | null;
}

export const defaultValue: Readonly<IPais> = {};
