import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pais.reducer';

export const PaisDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paisEntity = useAppSelector(state => state.pais.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paisDetailsHeading">
          <Translate contentKey="proyecto2025App.pais.detail.title">Pais</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paisEntity.id}</dd>
          <dt>
            <span id="nombrePais">
              <Translate contentKey="proyecto2025App.pais.nombrePais">Nombre Pais</Translate>
            </span>
          </dt>
          <dd>{paisEntity.nombrePais}</dd>
          <dt>
            <Translate contentKey="proyecto2025App.pais.region">Region</Translate>
          </dt>
          <dd>{paisEntity.region ? paisEntity.region.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pais" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pais/${paisEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaisDetail;
