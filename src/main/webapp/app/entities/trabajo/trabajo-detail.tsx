import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trabajo.reducer';

export const TrabajoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const trabajoEntity = useAppSelector(state => state.trabajo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="trabajoDetailsHeading">
          <Translate contentKey="proyecto2025App.trabajo.detail.title">Trabajo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{trabajoEntity.id}</dd>
          <dt>
            <span id="tituloTrabajo">
              <Translate contentKey="proyecto2025App.trabajo.tituloTrabajo">Titulo Trabajo</Translate>
            </span>
          </dt>
          <dd>{trabajoEntity.tituloTrabajo}</dd>
          <dt>
            <span id="salarioMin">
              <Translate contentKey="proyecto2025App.trabajo.salarioMin">Salario Min</Translate>
            </span>
          </dt>
          <dd>{trabajoEntity.salarioMin}</dd>
          <dt>
            <span id="salarioMax">
              <Translate contentKey="proyecto2025App.trabajo.salarioMax">Salario Max</Translate>
            </span>
          </dt>
          <dd>{trabajoEntity.salarioMax}</dd>
          <dt>
            <Translate contentKey="proyecto2025App.trabajo.tarea">Tarea</Translate>
          </dt>
          <dd>
            {trabajoEntity.tareas
              ? trabajoEntity.tareas.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.titulo}</a>
                    {trabajoEntity.tareas && i === trabajoEntity.tareas.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="proyecto2025App.trabajo.empleado">Empleado</Translate>
          </dt>
          <dd>{trabajoEntity.empleado ? trabajoEntity.empleado.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/trabajo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trabajo/${trabajoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TrabajoDetail;
