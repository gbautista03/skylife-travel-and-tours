import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './ohdc.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OHDCDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const oHDCEntity = useAppSelector(state => state.oHDC.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="oHDCDetailsHeading">OHDC</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{oHDCEntity.id}</dd>
          <dt>
            <span id="destination">Destination</span>
          </dt>
          <dd>{oHDCEntity.destination}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{oHDCEntity.description}</dd>
          <dt>
            <span id="contactDescription">Contact Description</span>
          </dt>
          <dd>{oHDCEntity.contactDescription}</dd>
        </dl>
        <Button tag={Link} to="/ohdc" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ohdc/${oHDCEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OHDCDetail;
