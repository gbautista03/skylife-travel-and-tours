import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPackageInclusionsExclusions } from 'app/shared/model/package-inclusions-exclusions.model';
import { getEntities as getPackageInclusionsExclusions } from 'app/entities/package-inclusions-exclusions/package-inclusions-exclusions.reducer';
import { IRequirements } from 'app/shared/model/requirements.model';
import { getEntities as getRequirements } from 'app/entities/requirements/requirements.reducer';
import { IOHDC } from 'app/shared/model/ohdc.model';
import { getEntities as getOHdcs } from 'app/entities/ohdc/ohdc.reducer';
import { IPassenger } from 'app/shared/model/passenger.model';
import { getEntities as getPassengers } from 'app/entities/passenger/passenger.reducer';
import { IFlightDetails } from 'app/shared/model/flight-details.model';
import { getEntities as getFlightDetails } from 'app/entities/flight-details/flight-details.reducer';
import { getEntity, updateEntity, createEntity, reset } from './package-tour.reducer';
import { IPackageTour } from 'app/shared/model/package-tour.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PackageTourUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const packageInclusionsExclusions = useAppSelector(state => state.packageInclusionsExclusions.entities);
  const requirements = useAppSelector(state => state.requirements.entities);
  const oHDCS = useAppSelector(state => state.oHDC.entities);
  const passengers = useAppSelector(state => state.passenger.entities);
  const flightDetails = useAppSelector(state => state.flightDetails.entities);
  const packageTourEntity = useAppSelector(state => state.packageTour.entity);
  const loading = useAppSelector(state => state.packageTour.loading);
  const updating = useAppSelector(state => state.packageTour.updating);
  const updateSuccess = useAppSelector(state => state.packageTour.updateSuccess);
  const handleClose = () => {
    props.history.push('/package-tour');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPackageInclusionsExclusions({}));
    dispatch(getRequirements({}));
    dispatch(getOHdcs({}));
    dispatch(getPassengers({}));
    dispatch(getFlightDetails({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...packageTourEntity,
      ...values,
      passengers: mapIdList(values.passengers),
      flightDetails: mapIdList(values.flightDetails),
      inclusionExclusion: packageInclusionsExclusions.find(it => it.id.toString() === values.inclusionExclusion.toString()),
      requirements: requirements.find(it => it.id.toString() === values.requirements.toString()),
      ohdc: oHDCS.find(it => it.id.toString() === values.ohdc.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...packageTourEntity,
          inclusionExclusion: packageTourEntity?.inclusionExclusion?.id,
          requirements: packageTourEntity?.requirements?.id,
          ohdc: packageTourEntity?.ohdc?.id,
          passengers: packageTourEntity?.passengers?.map(e => e.id.toString()),
          flightDetails: packageTourEntity?.flightDetails?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="skylifeTravelAndToursApp.packageTour.home.createOrEditLabel" data-cy="PackageTourCreateUpdateHeading">
            Create or edit a PackageTour
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="package-tour-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Days"
                id="package-tour-days"
                name="days"
                data-cy="days"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Nights"
                id="package-tour-nights"
                name="nights"
                data-cy="nights"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Destination"
                id="package-tour-destination"
                name="destination"
                data-cy="destination"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Tour Code"
                id="package-tour-tourCode"
                name="tourCode"
                data-cy="tourCode"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Date"
                id="package-tour-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Hotel"
                id="package-tour-hotel"
                name="hotel"
                data-cy="hotel"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Room Type"
                id="package-tour-roomType"
                name="roomType"
                data-cy="roomType"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Number Of Guest"
                id="package-tour-numberOfGuest"
                name="numberOfGuest"
                data-cy="numberOfGuest"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                id="package-tour-inclusionExclusion"
                name="inclusionExclusion"
                data-cy="inclusionExclusion"
                label="Inclusion Exclusion"
                type="select"
              >
                <option value="" key="0" />
                {packageInclusionsExclusions
                  ? packageInclusionsExclusions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.destination}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="package-tour-requirements" name="requirements" data-cy="requirements" label="Requirements" type="select">
                <option value="" key="0" />
                {requirements
                  ? requirements.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.destination}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="package-tour-ohdc" name="ohdc" data-cy="ohdc" label="Ohdc" type="select">
                <option value="" key="0" />
                {oHDCS
                  ? oHDCS.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.destination}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Passenger" id="package-tour-passenger" data-cy="passenger" type="select" multiple name="passengers">
                <option value="" key="0" />
                {passengers
                  ? passengers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.firstName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label="Flight Details"
                id="package-tour-flightDetails"
                data-cy="flightDetails"
                type="select"
                multiple
                name="flightDetails"
              >
                <option value="" key="0" />
                {flightDetails
                  ? flightDetails.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/package-tour" replace color="info">
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

export default PackageTourUpdate;
