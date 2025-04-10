import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './contrato.reducer';

export const Contrato = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const contratoList = useAppSelector(state => state.contrato.entities);
  const loading = useAppSelector(state => state.contrato.loading);
  const links = useAppSelector(state => state.contrato.links);
  const updateSuccess = useAppSelector(state => state.contrato.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="contrato-heading" data-cy="ContratoHeading">
        <Translate contentKey="proyecto2025App.contrato.home.title">Contratoes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="proyecto2025App.contrato.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/contrato/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="proyecto2025App.contrato.home.createLabel">Create new Contrato</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={contratoList ? contratoList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {contratoList && contratoList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="proyecto2025App.contrato.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('fechaInicio')}>
                    <Translate contentKey="proyecto2025App.contrato.fechaInicio">Fecha Inicio</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('fechaInicio')} />
                  </th>
                  <th className="hand" onClick={sort('fechaFin')}>
                    <Translate contentKey="proyecto2025App.contrato.fechaFin">Fecha Fin</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('fechaFin')} />
                  </th>
                  <th className="hand" onClick={sort('lenguaje')}>
                    <Translate contentKey="proyecto2025App.contrato.lenguaje">Lenguaje</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('lenguaje')} />
                  </th>
                  <th>
                    <Translate contentKey="proyecto2025App.contrato.trabajo">Trabajo</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="proyecto2025App.contrato.departamento">Departamento</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="proyecto2025App.contrato.empleado">Empleado</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {contratoList.map((contrato, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/contrato/${contrato.id}`} color="link" size="sm">
                        {contrato.id}
                      </Button>
                    </td>
                    <td>
                      {contrato.fechaInicio ? <TextFormat type="date" value={contrato.fechaInicio} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{contrato.fechaFin ? <TextFormat type="date" value={contrato.fechaFin} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>
                      <Translate contentKey={`proyecto2025App.Idioma.${contrato.lenguaje}`} />
                    </td>
                    <td>{contrato.trabajo ? <Link to={`/trabajo/${contrato.trabajo.id}`}>{contrato.trabajo.id}</Link> : ''}</td>
                    <td>
                      {contrato.departamento ? (
                        <Link to={`/departamento/${contrato.departamento.id}`}>{contrato.departamento.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>{contrato.empleado ? <Link to={`/empleado/${contrato.empleado.id}`}>{contrato.empleado.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/contrato/${contrato.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/contrato/${contrato.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/contrato/${contrato.id}/delete`)}
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
                <Translate contentKey="proyecto2025App.contrato.home.notFound">No Contratoes found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Contrato;
