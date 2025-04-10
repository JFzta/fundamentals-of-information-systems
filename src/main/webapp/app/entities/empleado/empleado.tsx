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

import { getEntities, reset } from './empleado.reducer';

export const Empleado = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const empleadoList = useAppSelector(state => state.empleado.entities);
  const loading = useAppSelector(state => state.empleado.loading);
  const links = useAppSelector(state => state.empleado.links);
  const updateSuccess = useAppSelector(state => state.empleado.updateSuccess);

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
      <h2 id="empleado-heading" data-cy="EmpleadoHeading">
        <Translate contentKey="proyecto2025App.empleado.home.title">Empleados</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="proyecto2025App.empleado.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/empleado/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="proyecto2025App.empleado.home.createLabel">Create new Empleado</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={empleadoList ? empleadoList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {empleadoList && empleadoList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="proyecto2025App.empleado.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('nombres')}>
                    <Translate contentKey="proyecto2025App.empleado.nombres">Nombres</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('nombres')} />
                  </th>
                  <th className="hand" onClick={sort('apellidos')}>
                    <Translate contentKey="proyecto2025App.empleado.apellidos">Apellidos</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('apellidos')} />
                  </th>
                  <th className="hand" onClick={sort('correo')}>
                    <Translate contentKey="proyecto2025App.empleado.correo">Correo</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('correo')} />
                  </th>
                  <th className="hand" onClick={sort('nroCelular')}>
                    <Translate contentKey="proyecto2025App.empleado.nroCelular">Nro Celular</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('nroCelular')} />
                  </th>
                  <th className="hand" onClick={sort('fechacontrato')}>
                    <Translate contentKey="proyecto2025App.empleado.fechacontrato">Fechacontrato</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('fechacontrato')} />
                  </th>
                  <th className="hand" onClick={sort('salario')}>
                    <Translate contentKey="proyecto2025App.empleado.salario">Salario</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('salario')} />
                  </th>
                  <th className="hand" onClick={sort('comisionPorcentaje')}>
                    <Translate contentKey="proyecto2025App.empleado.comisionPorcentaje">Comision Porcentaje</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('comisionPorcentaje')} />
                  </th>
                  <th>
                    <Translate contentKey="proyecto2025App.empleado.inmediatosuperior">Inmediatosuperior</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="proyecto2025App.empleado.departamento">Departamento</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {empleadoList.map((empleado, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/empleado/${empleado.id}`} color="link" size="sm">
                        {empleado.id}
                      </Button>
                    </td>
                    <td>{empleado.nombres}</td>
                    <td>{empleado.apellidos}</td>
                    <td>{empleado.correo}</td>
                    <td>{empleado.nroCelular}</td>
                    <td>
                      {empleado.fechacontrato ? <TextFormat type="date" value={empleado.fechacontrato} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{empleado.salario}</td>
                    <td>{empleado.comisionPorcentaje}</td>
                    <td>
                      {empleado.inmediatosuperior ? (
                        <Link to={`/empleado/${empleado.inmediatosuperior.id}`}>{empleado.inmediatosuperior.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {empleado.departamento ? (
                        <Link to={`/departamento/${empleado.departamento.id}`}>{empleado.departamento.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/empleado/${empleado.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/empleado/${empleado.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/empleado/${empleado.id}/delete`)}
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
                <Translate contentKey="proyecto2025App.empleado.home.notFound">No Empleados found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Empleado;
