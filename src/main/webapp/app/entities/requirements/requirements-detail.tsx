import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './requirements.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RequirementsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const requirementsEntity = useAppSelector(state => state.requirements.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="requirementsDetailsHeading">Requirements</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{requirementsEntity.id}</dd>
          <dt>
            <span id="destination">Destination</span>
          </dt>
          <dd>{requirementsEntity.destination}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{requirementsEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/requirements" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/requirements/${requirementsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RequirementsDetail;
