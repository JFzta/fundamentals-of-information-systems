import dayjs from 'dayjs';
import { ITrabajo } from 'app/shared/model/trabajo.model';
import { IDepartamento } from 'app/shared/model/departamento.model';
import { IEmpleado } from 'app/shared/model/empleado.model';
import { Idioma } from 'app/shared/model/enumerations/idioma.model';

export interface IContrato {
  id?: number;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  lenguaje?: keyof typeof Idioma | null;
  trabajo?: ITrabajo | null;
  departamento?: IDepartamento | null;
  empleado?: IEmpleado | null;
}

export const defaultValue: Readonly<IContrato> = {};
