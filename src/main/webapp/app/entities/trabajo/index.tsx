import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Trabajo from './trabajo';
import TrabajoDetail from './trabajo-detail';
import TrabajoUpdate from './trabajo-update';
import TrabajoDeleteDialog from './trabajo-delete-dialog';

const TrabajoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Trabajo />} />
    <Route path="new" element={<TrabajoUpdate />} />
    <Route path=":id">
      <Route index element={<TrabajoDetail />} />
      <Route path="edit" element={<TrabajoUpdate />} />
      <Route path="delete" element={<TrabajoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TrabajoRoutes;
