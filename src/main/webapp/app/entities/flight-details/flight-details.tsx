import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './flight-details.reducer';
import { IFlightDetails } from 'app/shared/model/flight-details.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FlightDetails = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const flightDetailsList = useAppSelector(state => state.flightDetails.entities);
  const loading = useAppSelector(state => state.flightDetails.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="flight-details-heading" data-cy="FlightDetailsHeading">
        Flight Details
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Flight Details
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {flightDetailsList && flightDetailsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Origin</th>
                <th>Destination</th>
                <th>Flight Number</th>
                <th>Carrier</th>
                <th>Departure Date</th>
                <th>Arrival Date</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {flightDetailsList.map((flightDetails, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${flightDetails.id}`} color="link" size="sm">
                      {flightDetails.id}
                    </Button>
                  </td>
                  <td>{flightDetails.origin}</td>
                  <td>{flightDetails.destination}</td>
                  <td>{flightDetails.flightNumber}</td>
                  <td>{flightDetails.carrier}</td>
                  <td>
                    {flightDetails.departureDate ? (
                      <TextFormat type="date" value={flightDetails.departureDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {flightDetails.arrivalDate ? (
                      <TextFormat type="date" value={flightDetails.arrivalDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${flightDetails.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${flightDetails.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${flightDetails.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Flight Details found</div>
        )}
      </div>
    </div>
  );
};

export default FlightDetails;
