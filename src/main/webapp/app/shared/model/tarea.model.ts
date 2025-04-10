import { ITrabajo } from 'app/shared/model/trabajo.model';

export interface ITarea {
  id?: number;
  titulo?: string | null;
  descripcion?: string | null;
  trabajos?: ITrabajo[] | null;
}

export const defaultValue: Readonly<ITarea> = {};
