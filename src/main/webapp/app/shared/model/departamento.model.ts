import { IDireccion } from 'app/shared/model/direccion.model';

export interface IDepartamento {
  id?: number;
  nombreDepartamento?: string;
  direccion?: IDireccion | null;
}

export const defaultValue: Readonly<IDepartamento> = {};
