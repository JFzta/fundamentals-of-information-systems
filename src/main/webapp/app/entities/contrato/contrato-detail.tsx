import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './contrato.reducer';

export const ContratoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contratoEntity = useAppSelector(state => state.contrato.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contratoDetailsHeading">
          <Translate contentKey="proyecto2025App.contrato.detail.title">Contrato</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contratoEntity.id}</dd>
          <dt>
            <span id="fechaInicio">
              <Translate contentKey="proyecto2025App.contrato.fechaInicio">Fecha Inicio</Translate>
            </span>
          </dt>
          <dd>
            {contratoEntity.fechaInicio ? <TextFormat value={contratoEntity.fechaInicio} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="fechaFin">
              <Translate contentKey="proyecto2025App.contrato.fechaFin">Fecha Fin</Translate>
            </span>
          </dt>
          <dd>{contratoEntity.fechaFin ? <TextFormat value={contratoEntity.fechaFin} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lenguaje">
              <Translate contentKey="proyecto2025App.contrato.lenguaje">Lenguaje</Translate>
            </span>
          </dt>
          <dd>{contratoEntity.lenguaje}</dd>
          <dt>
            <Translate contentKey="proyecto2025App.contrato.trabajo">Trabajo</Translate>
          </dt>
          <dd>{contratoEntity.trabajo ? contratoEntity.trabajo.id : ''}</dd>
          <dt>
            <Translate contentKey="proyecto2025App.contrato.departamento">Departamento</Translate>
          </dt>
          <dd>{contratoEntity.departamento ? contratoEntity.departamento.id : ''}</dd>
          <dt>
            <Translate contentKey="proyecto2025App.contrato.empleado">Empleado</Translate>
          </dt>
          <dd>{contratoEntity.empleado ? contratoEntity.empleado.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/contrato" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contrato/${contratoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContratoDetail;
