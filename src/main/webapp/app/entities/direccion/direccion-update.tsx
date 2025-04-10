import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPais } from 'app/entities/pais/pais.reducer';
import { createEntity, getEntity, reset, updateEntity } from './direccion.reducer';

export const DireccionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pais = useAppSelector(state => state.pais.entities);
  const direccionEntity = useAppSelector(state => state.direccion.entity);
  const loading = useAppSelector(state => state.direccion.loading);
  const updating = useAppSelector(state => state.direccion.updating);
  const updateSuccess = useAppSelector(state => state.direccion.updateSuccess);

  const handleClose = () => {
    navigate('/direccion');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPais({}));
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
      ...direccionEntity,
      ...values,
      pais: pais.find(it => it.id.toString() === values.pais?.toString()),
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
          ...direccionEntity,
          pais: direccionEntity?.pais?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="proyecto2025App.direccion.home.createOrEditLabel" data-cy="DireccionCreateUpdateHeading">
            <Translate contentKey="proyecto2025App.direccion.home.createOrEditLabel">Create or edit a Direccion</Translate>
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
                  id="direccion-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('proyecto2025App.direccion.calle')}
                id="direccion-calle"
                name="calle"
                data-cy="calle"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.direccion.codigoPostal')}
                id="direccion-codigoPostal"
                name="codigoPostal"
                data-cy="codigoPostal"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.direccion.ciudad')}
                id="direccion-ciudad"
                name="ciudad"
                data-cy="ciudad"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.direccion.provincia')}
                id="direccion-provincia"
                name="provincia"
                data-cy="provincia"
                type="text"
              />
              <ValidatedField
                id="direccion-pais"
                name="pais"
                data-cy="pais"
                label={translate('proyecto2025App.direccion.pais')}
                type="select"
              >
                <option value="" key="0" />
                {pais
                  ? pais.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/direccion" replace color="info">
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

export default DireccionUpdate;
