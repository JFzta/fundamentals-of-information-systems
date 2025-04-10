import { IPais } from 'app/shared/model/pais.model';

export interface IDireccion {
  id?: number;
  calle?: string | null;
  codigoPostal?: string | null;
  ciudad?: string | null;
  provincia?: string | null;
  pais?: IPais | null;
}

export const defaultValue: Readonly<IDireccion> = {};
