import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getEmpleados } from 'app/entities/empleado/empleado.reducer';
import { getEntities as getDepartamentos } from 'app/entities/departamento/departamento.reducer';
import { createEntity, getEntity, updateEntity } from './empleado.reducer';

export const EmpleadoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const empleados = useAppSelector(state => state.empleado.entities);
  const departamentos = useAppSelector(state => state.departamento.entities);
  const empleadoEntity = useAppSelector(state => state.empleado.entity);
  const loading = useAppSelector(state => state.empleado.loading);
  const updating = useAppSelector(state => state.empleado.updating);
  const updateSuccess = useAppSelector(state => state.empleado.updateSuccess);

  const handleClose = () => {
    navigate('/empleado');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getEmpleados({}));
    dispatch(getDepartamentos({}));
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
    values.fechacontrato = convertDateTimeToServer(values.fechacontrato);
    if (values.salario !== undefined && typeof values.salario !== 'number') {
      values.salario = Number(values.salario);
    }
    if (values.comisionPorcentaje !== undefined && typeof values.comisionPorcentaje !== 'number') {
      values.comisionPorcentaje = Number(values.comisionPorcentaje);
    }

    const entity = {
      ...empleadoEntity,
      ...values,
      inmediatosuperior: empleados.find(it => it.id.toString() === values.inmediatosuperior?.toString()),
      departamento: departamentos.find(it => it.id.toString() === values.departamento?.toString()),
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
          fechacontrato: displayDefaultDateTime(),
        }
      : {
          ...empleadoEntity,
          fechacontrato: convertDateTimeFromServer(empleadoEntity.fechacontrato),
          inmediatosuperior: empleadoEntity?.inmediatosuperior?.id,
          departamento: empleadoEntity?.departamento?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="proyecto2025App.empleado.home.createOrEditLabel" data-cy="EmpleadoCreateUpdateHeading">
            <Translate contentKey="proyecto2025App.empleado.home.createOrEditLabel">Create or edit a Empleado</Translate>
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
                  id="empleado-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('proyecto2025App.empleado.nombres')}
                id="empleado-nombres"
                name="nombres"
                data-cy="nombres"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.empleado.apellidos')}
                id="empleado-apellidos"
                name="apellidos"
                data-cy="apellidos"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.empleado.correo')}
                id="empleado-correo"
                name="correo"
                data-cy="correo"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.empleado.nroCelular')}
                id="empleado-nroCelular"
                name="nroCelular"
                data-cy="nroCelular"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.empleado.fechacontrato')}
                id="empleado-fechacontrato"
                name="fechacontrato"
                data-cy="fechacontrato"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('proyecto2025App.empleado.salario')}
                id="empleado-salario"
                name="salario"
                data-cy="salario"
                type="text"
              />
              <ValidatedField
                label={translate('proyecto2025App.empleado.comisionPorcentaje')}
                id="empleado-comisionPorcentaje"
                name="comisionPorcentaje"
                data-cy="comisionPorcentaje"
                type="text"
              />
              <ValidatedField
                id="empleado-inmediatosuperior"
                name="inmediatosuperior"
                data-cy="inmediatosuperior"
                label={translate('proyecto2025App.empleado.inmediatosuperior')}
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
              <ValidatedField
                id="empleado-departamento"
                name="departamento"
                data-cy="departamento"
                label={translate('proyecto2025App.empleado.departamento')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/empleado" replace color="info">
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

export default EmpleadoUpdate;
