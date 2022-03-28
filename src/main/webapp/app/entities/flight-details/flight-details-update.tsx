import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPackageTour } from 'app/shared/model/package-tour.model';
import { getEntities as getPackageTours } from 'app/entities/package-tour/package-tour.reducer';
import { getEntity, updateEntity, createEntity, reset } from './flight-details.reducer';
import { IFlightDetails } from 'app/shared/model/flight-details.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FlightDetailsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const packageTours = useAppSelector(state => state.packageTour.entities);
  const flightDetailsEntity = useAppSelector(state => state.flightDetails.entity);
  const loading = useAppSelector(state => state.flightDetails.loading);
  const updating = useAppSelector(state => state.flightDetails.updating);
  const updateSuccess = useAppSelector(state => state.flightDetails.updateSuccess);
  const handleClose = () => {
    props.history.push('/flight-details');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPackageTours({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.departureDate = convertDateTimeToServer(values.departureDate);
    values.arrivalDate = convertDateTimeToServer(values.arrivalDate);

    const entity = {
      ...flightDetailsEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          departureDate: displayDefaultDateTime(),
          arrivalDate: displayDefaultDateTime(),
        }
      : {
          ...flightDetailsEntity,
          departureDate: convertDateTimeFromServer(flightDetailsEntity.departureDate),
          arrivalDate: convertDateTimeFromServer(flightDetailsEntity.arrivalDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="skylifeTravelAndToursApp.flightDetails.home.createOrEditLabel" data-cy="FlightDetailsCreateUpdateHeading">
            Create or edit a FlightDetails
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="flight-details-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Origin"
                id="flight-details-origin"
                name="origin"
                data-cy="origin"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Destination"
                id="flight-details-destination"
                name="destination"
                data-cy="destination"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Flight Number"
                id="flight-details-flightNumber"
                name="flightNumber"
                data-cy="flightNumber"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Carrier"
                id="flight-details-carrier"
                name="carrier"
                data-cy="carrier"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Departure Date"
                id="flight-details-departureDate"
                name="departureDate"
                data-cy="departureDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Arrival Date"
                id="flight-details-arrivalDate"
                name="arrivalDate"
                data-cy="arrivalDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/flight-details" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FlightDetailsUpdate;
