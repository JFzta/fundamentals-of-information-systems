import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Contrato from './contrato';
import ContratoDetail from './contrato-detail';
import ContratoUpdate from './contrato-update';
import ContratoDeleteDialog from './contrato-delete-dialog';

const ContratoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Contrato />} />
    <Route path="new" element={<ContratoUpdate />} />
    <Route path=":id">
      <Route index element={<ContratoDetail />} />
      <Route path="edit" element={<ContratoUpdate />} />
      <Route path="delete" element={<ContratoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ContratoRoutes;
