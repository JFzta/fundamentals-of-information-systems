import dayjs from 'dayjs';
import { IDepartamento } from 'app/shared/model/departamento.model';

export interface IEmpleado {
  id?: number;
  nombres?: string | null;
  apellidos?: string | null;
  correo?: string | null;
  nroCelular?: string | null;
  fechacontrato?: dayjs.Dayjs | null;
  salario?: number | null;
  comisionPorcentaje?: number | null;
  inmediatosuperior?: IEmpleado | null;
  departamento?: IDepartamento | null;
}

export const defaultValue: Readonly<IEmpleado> = {};
