import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FlightDetails from './flight-details';
import FlightDetailsDetail from './flight-details-detail';
import FlightDetailsUpdate from './flight-details-update';
import FlightDetailsDeleteDialog from './flight-details-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FlightDetailsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FlightDetailsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FlightDetailsDetail} />
      <ErrorBoundaryRoute path={match.url} component={FlightDetails} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FlightDetailsDeleteDialog} />
  </>
);

export default Routes;
