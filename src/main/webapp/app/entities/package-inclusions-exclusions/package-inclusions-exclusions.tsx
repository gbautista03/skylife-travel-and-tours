import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './package-inclusions-exclusions.reducer';
import { IPackageInclusionsExclusions } from 'app/shared/model/package-inclusions-exclusions.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PackageInclusionsExclusions = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const packageInclusionsExclusionsList = useAppSelector(state => state.packageInclusionsExclusions.entities);
  const loading = useAppSelector(state => state.packageInclusionsExclusions.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="package-inclusions-exclusions-heading" data-cy="PackageInclusionsExclusionsHeading">
        Package Inclusions Exclusions
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Package Inclusions Exclusions
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {packageInclusionsExclusionsList && packageInclusionsExclusionsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Destination</th>
                <th>Inclusions</th>
                <th>Exclusions</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {packageInclusionsExclusionsList.map((packageInclusionsExclusions, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${packageInclusionsExclusions.id}`} color="link" size="sm">
                      {packageInclusionsExclusions.id}
                    </Button>
                  </td>
                  <td>{packageInclusionsExclusions.destination}</td>
                  <td>{packageInclusionsExclusions.inclusions}</td>
                  <td>{packageInclusionsExclusions.exclusions}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`${match.url}/${packageInclusionsExclusions.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${packageInclusionsExclusions.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${packageInclusionsExclusions.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Package Inclusions Exclusions found</div>
        )}
      </div>
    </div>
  );
};

export default PackageInclusionsExclusions;
