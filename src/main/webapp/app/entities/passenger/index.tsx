import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Passenger from './passenger';
import PassengerDetail from './passenger-detail';
import PassengerUpdate from './passenger-update';
import PassengerDeleteDialog from './passenger-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PassengerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PassengerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PassengerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Passenger} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PassengerDeleteDialog} />
  </>
);

export default Routes;
