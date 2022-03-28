import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './passenger.reducer';
import { IPassenger } from 'app/shared/model/passenger.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Passenger = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const passengerList = useAppSelector(state => state.passenger.entities);
  const loading = useAppSelector(state => state.passenger.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="passenger-heading" data-cy="PassengerHeading">
        Passengers
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Passenger
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {passengerList && passengerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Birthday</th>
                <th>Gender</th>
                <th>Citizenship</th>
                <th>Contact Number</th>
                <th>Email Address</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {passengerList.map((passenger, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${passenger.id}`} color="link" size="sm">
                      {passenger.id}
                    </Button>
                  </td>
                  <td>{passenger.firstName}</td>
                  <td>{passenger.lastName}</td>
                  <td>
                    {passenger.birthday ? <TextFormat type="date" value={passenger.birthday} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{passenger.gender}</td>
                  <td>{passenger.citizenship}</td>
                  <td>{passenger.contactNumber}</td>
                  <td>{passenger.emailAddress}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${passenger.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${passenger.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${passenger.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Passengers found</div>
        )}
      </div>
    </div>
  );
};

export default Passenger;
