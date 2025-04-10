import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './direccion.reducer';

export const DireccionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const direccionEntity = useAppSelector(state => state.direccion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="direccionDetailsHeading">
          <Translate contentKey="proyecto2025App.direccion.detail.title">Direccion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{direccionEntity.id}</dd>
          <dt>
            <span id="calle">
              <Translate contentKey="proyecto2025App.direccion.calle">Calle</Translate>
            </span>
          </dt>
          <dd>{direccionEntity.calle}</dd>
          <dt>
            <span id="codigoPostal">
              <Translate contentKey="proyecto2025App.direccion.codigoPostal">Codigo Postal</Translate>
            </span>
          </dt>
          <dd>{direccionEntity.codigoPostal}</dd>
          <dt>
            <span id="ciudad">
              <Translate contentKey="proyecto2025App.direccion.ciudad">Ciudad</Translate>
            </span>
          </dt>
          <dd>{direccionEntity.ciudad}</dd>
          <dt>
            <span id="provincia">
              <Translate contentKey="proyecto2025App.direccion.provincia">Provincia</Translate>
            </span>
          </dt>
          <dd>{direccionEntity.provincia}</dd>
          <dt>
            <Translate contentKey="proyecto2025App.direccion.pais">Pais</Translate>
          </dt>
          <dd>{direccionEntity.pais ? direccionEntity.pais.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/direccion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/direccion/${direccionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DireccionDetail;
