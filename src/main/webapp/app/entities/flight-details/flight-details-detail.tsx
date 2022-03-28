import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './flight-details.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FlightDetailsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const flightDetailsEntity = useAppSelector(state => state.flightDetails.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="flightDetailsDetailsHeading">FlightDetails</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{flightDetailsEntity.id}</dd>
          <dt>
            <span id="origin">Origin</span>
          </dt>
          <dd>{flightDetailsEntity.origin}</dd>
          <dt>
            <span id="destination">Destination</span>
          </dt>
          <dd>{flightDetailsEntity.destination}</dd>
          <dt>
            <span id="flightNumber">Flight Number</span>
          </dt>
          <dd>{flightDetailsEntity.flightNumber}</dd>
          <dt>
            <span id="carrier">Carrier</span>
          </dt>
          <dd>{flightDetailsEntity.carrier}</dd>
          <dt>
            <span id="departureDate">Departure Date</span>
          </dt>
          <dd>
            {flightDetailsEntity.departureDate ? (
              <TextFormat value={flightDetailsEntity.departureDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="arrivalDate">Arrival Date</span>
          </dt>
          <dd>
            {flightDetailsEntity.arrivalDate ? (
              <TextFormat value={flightDetailsEntity.arrivalDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/flight-details" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/flight-details/${flightDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FlightDetailsDetail;
