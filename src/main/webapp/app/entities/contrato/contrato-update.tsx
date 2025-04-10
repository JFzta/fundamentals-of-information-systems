import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTrabajos } from 'app/entities/trabajo/trabajo.reducer';
import { getEntities as getDepartamentos } from 'app/entities/departamento/departamento.reducer';
import { getEntities as getEmpleados } from 'app/entities/empleado/empleado.reducer';
import { Idioma } from 'app/shared/model/enumerations/idioma.model';
import { createEntity, getEntity, updateEntity } from './contrato.reducer';

export const ContratoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trabajos = useAppSelector(state => state.trabajo.entities);
  const departamentos = useAppSelector(state => state.departamento.entities);
  const empleados = useAppSelector(state => state.empleado.entities);
  const contratoEntity = useAppSelector(state => state.contrato.entity);
  const loading = useAppSelector(state => state.contrato.loading);
  const updating = useAppSelector(state => state.contrato.updating);
  const updateSuccess = useAppSelector(state => state.contrato.updateSuccess);
  const idiomaValues = Object.keys(Idioma);

  const handleClose = () => {
    navigate('/contrato');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getTrabajos({}));
    dispatch(getDepartamentos({}));
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
    values.fechaInicio = convertDateTimeToServer(values.fechaInicio);
    values.fechaFin = convertDateTimeToServer(values.fechaFin);

    const entity = {
      ...contratoEntity,
      ...values,
      trabajo: trabajos.find(it => it.id.toString() === values.trabajo?.toString()),
      departamento: departamentos.find(it => it.id.toString() === values.departamento?.toString()),
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
      ? {
          fechaInicio: displayDefaultDateTime(),
          fechaFin: displayDefaultDateTime(),
        }
      : {
          lenguaje: 'ESPANOL',
          ...contratoEntity,
          fechaInicio: convertDateTimeFromServer(contratoEntity.fechaInicio),
          fechaFin: convertDateTimeFromServer(contratoEntity.fechaFin),
          trabajo: contratoEntity?.trabajo?.id,
          departamento: contratoEntity?.departamento?.id,
          empleado: contratoEntity?.empleado?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="proyecto2025App.contrato.home.createOrEditLabel" data-cy="ContratoCreateUpdateHeading">
            <Translate contentKey="proyecto2025App.contrato.home.createOrEditLabel">Create or edit a Contrato</Translate>
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
                  id="contrato-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('proyecto2025App.contrato.fechaInicio')}
                id="contrato-fechaInicio"
                name="fechaInicio"
                data-cy="fechaInicio"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('proyecto2025App.contrato.fechaFin')}
                id="contrato-fechaFin"
                name="fechaFin"
                data-cy="fechaFin"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('proyecto2025App.contrato.lenguaje')}
                id="contrato-lenguaje"
                name="lenguaje"
                data-cy="lenguaje"
                type="select"
              >
                {idiomaValues.map(idioma => (
                  <option value={idioma} key={idioma}>
                    {translate(`proyecto2025App.Idioma.${idioma}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="contrato-trabajo"
                name="trabajo"
                data-cy="trabajo"
                label={translate('proyecto2025App.contrato.trabajo')}
                type="select"
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
              <ValidatedField
                id="contrato-departamento"
                name="departamento"
                data-cy="departamento"
                label={translate('proyecto2025App.contrato.departamento')}
                type="select"
              >
                <option value="" key="0" />
                {departamentos
                  ? departamentos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="contrato-empleado"
                name="empleado"
                data-cy="empleado"
                label={translate('proyecto2025App.contrato.empleado')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/contrato" replace color="info">
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

export default ContratoUpdate;
