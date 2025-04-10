import { ITarea } from 'app/shared/model/tarea.model';
import { IEmpleado } from 'app/shared/model/empleado.model';

export interface ITrabajo {
  id?: number;
  tituloTrabajo?: string | null;
  salarioMin?: number | null;
  salarioMax?: number | null;
  tareas?: ITarea[] | null;
  empleado?: IEmpleado | null;
}

export const defaultValue: Readonly<ITrabajo> = {};
