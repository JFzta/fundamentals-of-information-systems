import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './direccion.reducer';

export const Direccion = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const direccionList = useAppSelector(state => state.direccion.entities);
  const loading = useAppSelector(state => state.direccion.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="direccion-heading" data-cy="DireccionHeading">
        <Translate contentKey="proyecto2025App.direccion.home.title">Direccions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="proyecto2025App.direccion.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/direccion/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="proyecto2025App.direccion.home.createLabel">Create new Direccion</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {direccionList && direccionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="proyecto2025App.direccion.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('calle')}>
                  <Translate contentKey="proyecto2025App.direccion.calle">Calle</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('calle')} />
                </th>
                <th className="hand" onClick={sort('codigoPostal')}>
                  <Translate contentKey="proyecto2025App.direccion.codigoPostal">Codigo Postal</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('codigoPostal')} />
                </th>
                <th className="hand" onClick={sort('ciudad')}>
                  <Translate contentKey="proyecto2025App.direccion.ciudad">Ciudad</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ciudad')} />
                </th>
                <th className="hand" onClick={sort('provincia')}>
                  <Translate contentKey="proyecto2025App.direccion.provincia">Provincia</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('provincia')} />
                </th>
                <th>
                  <Translate contentKey="proyecto2025App.direccion.pais">Pais</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {direccionList.map((direccion, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/direccion/${direccion.id}`} color="link" size="sm">
                      {direccion.id}
                    </Button>
                  </td>
                  <td>{direccion.calle}</td>
                  <td>{direccion.codigoPostal}</td>
                  <td>{direccion.ciudad}</td>
                  <td>{direccion.provincia}</td>
                  <td>{direccion.pais ? <Link to={`/pais/${direccion.pais.id}`}>{direccion.pais.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/direccion/${direccion.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/direccion/${direccion.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/direccion/${direccion.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="proyecto2025App.direccion.home.notFound">No Direccions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Direccion;
