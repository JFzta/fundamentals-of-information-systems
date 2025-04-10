import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTareas } from 'app/entities/tarea/tarea.reducer';
import { getEntities as getEmpleados } from 'app/entities/empleado/empleado.reducer';
import { createEntity, getEntity, reset, updateEntity } from './trabajo.reducer';

export const TrabajoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tareas = useAppSelector(state => state.tarea.entities);
  const empleados = useAppSelector(state => state.empleado.entities);
  const trabajoEntity = useAppSelector(state => state.trabajo.entity);
  const loading = useAppSelector(state => state.trabajo.loading);
  const updating = useAppSelector(state => state.trabajo.updating);
  const updateSuccess = useAppSelector(state => state.trabajo.updateSuccess);

  const handleClose = () => {
    navigate(`/trabajo${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTareas({}));
    dispatch(getEmpleados({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.salarioMin !== undefined && typeof values.salarioMin !== 'number') {
      values.salarioMin = Number(values.salarioMin);
    }
    if (values.salarioMax !== undefined && typeof values.salarioMax !== 'number') {
      values.salarioMax = Number(values.salarioMax);
    }

    const entity = {
      ...trabajoEntity,
      ...values,
      tareas: mapIdList(values.tareas),
      empleado: empleados.find(it => it.id.toString() === values.empleado?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...trabajoEntity,
          tareas: trabajoEntity?.tareas?.map(e => e.id.toString()),
          empleado: trabajoEntity?.empleado?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="proyecto2025App.trabajo.home.createOrEditLabel" data-cy="TrabajoCreateUpdateHeading">
            <Translate contentKey="proyecto2025App.trabajo.home.createOrEditLabel">Create or edit a Trabajo</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="trabajo-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('proyecto2025App.trabajo.tituloTrabajo')}
                id="trabajo-tituloTrabajo"
                name="tituloTrabajo"
                data-cy="tituloTrabajo"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.trabajo.salarioMin')}
                id="trabajo-salarioMin"
                name="salarioMin"
                data-cy="salarioMin"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.trabajo.salarioMax')}
                id="trabajo-salarioMax"
                name="salarioMax"
                data-cy="salarioMax"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.trabajo.tarea')}
                id="trabajo-tarea"
                data-cy="tarea"
                type="select"
                multiple
                name="tareas"
              >
                <option value="" key="0" />
                {tareas
                  ? tareas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.titulo}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="trabajo-empleado"
                name="empleado"
                data-cy="empleado"
                label={translate('proyecto2025App.trabajo.empleado')}
                type="select"
              >
                <option value="" key="0" />
                {empleados
                  ? empleados.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trabajo" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TrabajoUpdate;
