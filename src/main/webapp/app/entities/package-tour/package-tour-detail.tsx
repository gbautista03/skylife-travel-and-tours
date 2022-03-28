import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './package-tour.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PackageTourDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const packageTourEntity = useAppSelector(state => state.packageTour.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="packageTourDetailsHeading">PackageTour</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{packageTourEntity.id}</dd>
          <dt>
            <span id="days">Days</span>
          </dt>
          <dd>{packageTourEntity.days}</dd>
          <dt>
            <span id="nights">Nights</span>
          </dt>
          <dd>{packageTourEntity.nights}</dd>
          <dt>
            <span id="destination">Destination</span>
          </dt>
          <dd>{packageTourEntity.destination}</dd>
          <dt>
            <span id="tourCode">Tour Code</span>
          </dt>
          <dd>{packageTourEntity.tourCode}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>
            {packageTourEntity.date ? <TextFormat value={packageTourEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="hotel">Hotel</span>
          </dt>
          <dd>{packageTourEntity.hotel}</dd>
          <dt>
            <span id="roomType">Room Type</span>
          </dt>
          <dd>{packageTourEntity.roomType}</dd>
          <dt>
            <span id="numberOfGuest">Number Of Guest</span>
          </dt>
          <dd>{packageTourEntity.numberOfGuest}</dd>
          <dt>Inclusion Exclusion</dt>
          <dd>{packageTourEntity.inclusionExclusion ? packageTourEntity.inclusionExclusion.destination : ''}</dd>
          <dt>Requirements</dt>
          <dd>{packageTourEntity.requirements ? packageTourEntity.requirements.destination : ''}</dd>
          <dt>Ohdc</dt>
          <dd>{packageTourEntity.ohdc ? packageTourEntity.ohdc.destination : ''}</dd>
          <dt>Passenger</dt>
          <dd>
            {packageTourEntity.passengers
              ? packageTourEntity.passengers.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.firstName}</a>
                    {packageTourEntity.passengers && i === packageTourEntity.passengers.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Flight Details</dt>
          <dd>
            {packageTourEntity.flightDetails
              ? packageTourEntity.flightDetails.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {packageTourEntity.flightDetails && i === packageTourEntity.flightDetails.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/package-tour" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/package-tour/${packageTourEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PackageTourDetail;
