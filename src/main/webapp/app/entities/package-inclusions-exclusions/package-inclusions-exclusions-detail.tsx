import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './package-inclusions-exclusions.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PackageInclusionsExclusionsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const packageInclusionsExclusionsEntity = useAppSelector(state => state.packageInclusionsExclusions.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="packageInclusionsExclusionsDetailsHeading">PackageInclusionsExclusions</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{packageInclusionsExclusionsEntity.id}</dd>
          <dt>
            <span id="destination">Destination</span>
          </dt>
          <dd>{packageInclusionsExclusionsEntity.destination}</dd>
          <dt>
            <span id="inclusions">Inclusions</span>
          </dt>
          <dd>{packageInclusionsExclusionsEntity.inclusions}</dd>
          <dt>
            <span id="exclusions">Exclusions</span>
          </dt>
          <dd>{packageInclusionsExclusionsEntity.exclusions}</dd>
        </dl>
        <Button tag={Link} to="/package-inclusions-exclusions" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/package-inclusions-exclusions/${packageInclusionsExclusionsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PackageInclusionsExclusionsDetail;
