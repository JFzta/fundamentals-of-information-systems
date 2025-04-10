import region from 'app/entities/region/region.reducer';
import pais from 'app/entities/pais/pais.reducer';
import direccion from 'app/entities/direccion/direccion.reducer';
import departamento from 'app/entities/departamento/departamento.reducer';
import tarea from 'app/entities/tarea/tarea.reducer';
import empleado from 'app/entities/empleado/empleado.reducer';
import trabajo from 'app/entities/trabajo/trabajo.reducer';
import contrato from 'app/entities/contrato/contrato.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  region,
  pais,
  direccion,
  departamento,
  tarea,
  empleado,
  trabajo,
  contrato,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
