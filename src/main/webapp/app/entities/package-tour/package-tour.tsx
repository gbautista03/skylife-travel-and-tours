import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './package-tour.reducer';
import { IPackageTour } from 'app/shared/model/package-tour.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PackageTour = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const packageTourList = useAppSelector(state => state.packageTour.entities);
  const loading = useAppSelector(state => state.packageTour.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="package-tour-heading" data-cy="PackageTourHeading">
        Package Tours
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Package Tour
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {packageTourList && packageTourList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Days</th>
                <th>Nights</th>
                <th>Destination</th>
                <th>Tour Code</th>
                <th>Date</th>
                <th>Hotel</th>
                <th>Room Type</th>
                <th>Number Of Guest</th>
                <th>Inclusion Exclusion</th>
                <th>Requirements</th>
                <th>Ohdc</th>
                <th>Passenger</th>
                <th>Flight Details</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {packageTourList.map((packageTour, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${packageTour.id}`} color="link" size="sm">
                      {packageTour.id}
                    </Button>
                  </td>
                  <td>{packageTour.days}</td>
                  <td>{packageTour.nights}</td>
                  <td>{packageTour.destination}</td>
                  <td>{packageTour.tourCode}</td>
                  <td>{packageTour.date ? <TextFormat type="date" value={packageTour.date} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{packageTour.hotel}</td>
                  <td>{packageTour.roomType}</td>
                  <td>{packageTour.numberOfGuest}</td>
                  <td>
                    {packageTour.inclusionExclusion ? (
                      <Link to={`package-inclusions-exclusions/${packageTour.inclusionExclusion.id}`}>
                        {packageTour.inclusionExclusion.destination}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {packageTour.requirements ? (
                      <Link to={`requirements/${packageTour.requirements.id}`}>{packageTour.requirements.destination}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{packageTour.ohdc ? <Link to={`ohdc/${packageTour.ohdc.id}`}>{packageTour.ohdc.destination}</Link> : ''}</td>
                  <td>
                    {packageTour.passengers
                      ? packageTour.passengers.map((val, j) => (
                          <span key={j}>
                            <Link to={`passenger/${val.id}`}>{val.firstName}</Link>
                            {j === packageTour.passengers.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {packageTour.flightDetails
                      ? packageTour.flightDetails.map((val, j) => (
                          <span key={j}>
                            <Link to={`flight-details/${val.id}`}>{val.id}</Link>
                            {j === packageTour.flightDetails.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${packageTour.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${packageTour.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${packageTour.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Package Tours found</div>
        )}
      </div>
    </div>
  );
};

export default PackageTour;
