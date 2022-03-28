import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PackageTour from './package-tour';
import PackageTourDetail from './package-tour-detail';
import PackageTourUpdate from './package-tour-update';
import PackageTourDeleteDialog from './package-tour-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PackageTourUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PackageTourUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PackageTourDetail} />
      <ErrorBoundaryRoute path={match.url} component={PackageTour} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PackageTourDeleteDialog} />
  </>
);

export default Routes;
