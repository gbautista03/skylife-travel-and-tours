import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './passenger.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PassengerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const passengerEntity = useAppSelector(state => state.passenger.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="passengerDetailsHeading">Passenger</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{passengerEntity.id}</dd>
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{passengerEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{passengerEntity.lastName}</dd>
          <dt>
            <span id="birthday">Birthday</span>
          </dt>
          <dd>
            {passengerEntity.birthday ? <TextFormat value={passengerEntity.birthday} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="gender">Gender</span>
          </dt>
          <dd>{passengerEntity.gender}</dd>
          <dt>
            <span id="citizenship">Citizenship</span>
          </dt>
          <dd>{passengerEntity.citizenship}</dd>
          <dt>
            <span id="contactNumber">Contact Number</span>
          </dt>
          <dd>{passengerEntity.contactNumber}</dd>
          <dt>
            <span id="emailAddress">Email Address</span>
          </dt>
          <dd>{passengerEntity.emailAddress}</dd>
        </dl>
        <Button tag={Link} to="/passenger" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/passenger/${passengerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PassengerDetail;
