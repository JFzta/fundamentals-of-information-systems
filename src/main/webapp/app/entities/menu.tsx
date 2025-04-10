import React from 'react';
import { Translate } from 'react-jhipster';

import { MenuItem } from 'app/shared/layout/menus/menu-item'; // eslint-disable-line

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/region">
        <Translate contentKey="global.menu.entities.region" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pais">
        <Translate contentKey="global.menu.entities.pais" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/direccion">
        <Translate contentKey="global.menu.entities.direccion" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/departamento">
        <Translate contentKey="global.menu.entities.departamento" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tarea">
        <Translate contentKey="global.menu.entities.tarea" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/empleado">
        <Translate contentKey="global.menu.entities.empleado" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/trabajo">
        <Translate contentKey="global.menu.entities.trabajo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/contrato">
        <Translate contentKey="global.menu.entities.contrato" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
