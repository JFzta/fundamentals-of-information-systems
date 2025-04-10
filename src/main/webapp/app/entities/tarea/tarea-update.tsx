import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTrabajos } from 'app/entities/trabajo/trabajo.reducer';
import { createEntity, getEntity, reset, updateEntity } from './tarea.reducer';

export const TareaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trabajos = useAppSelector(state => state.trabajo.entities);
  const tareaEntity = useAppSelector(state => state.tarea.entity);
  const loading = useAppSelector(state => state.tarea.loading);
  const updating = useAppSelector(state => state.tarea.updating);
  const updateSuccess = useAppSelector(state => state.tarea.updateSuccess);

  const handleClose = () => {
    navigate('/tarea');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTrabajos({}));
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

    const entity = {
      ...tareaEntity,
      ...values,
      trabajos: mapIdList(values.trabajos),
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
          ...tareaEntity,
          trabajos: tareaEntity?.trabajos?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="proyecto2025App.tarea.home.createOrEditLabel" data-cy="TareaCreateUpdateHeading">
            <Translate contentKey="proyecto2025App.tarea.home.createOrEditLabel">Create or edit a Tarea</Translate>
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
                  id="tarea-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('proyecto2025App.tarea.titulo')}
                id="tarea-titulo"
                name="titulo"
                data-cy="titulo"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.tarea.descripcion')}
                id="tarea-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.tarea.trabajo')}
                id="tarea-trabajo"
                data-cy="trabajo"
                type="select"
                multiple
                name="trabajos"
              >
                <option value="" key="0" />
                {trabajos
                  ? trabajos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tarea" replace color="info">
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

export default TareaUpdate;
