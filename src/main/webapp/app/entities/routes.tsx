import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Region from './region';
import Pais from './pais';
import Direccion from './direccion';
import Departamento from './departamento';
import Tarea from './tarea';
import Empleado from './empleado';
import Trabajo from './trabajo';
import Contrato from './contrato';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="region/*" element={<Region />} />
        <Route path="pais/*" element={<Pais />} />
        <Route path="direccion/*" element={<Direccion />} />
        <Route path="departamento/*" element={<Departamento />} />
        <Route path="tarea/*" element={<Tarea />} />
        <Route path="empleado/*" element={<Empleado />} />
        <Route path="trabajo/*" element={<Trabajo />} />
        <Route path="contrato/*" element={<Contrato />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
