import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Requirements from './requirements';
import RequirementsDetail from './requirements-detail';
import RequirementsUpdate from './requirements-update';
import RequirementsDeleteDialog from './requirements-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RequirementsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RequirementsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RequirementsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Requirements} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RequirementsDeleteDialog} />
  </>
);

export default Routes;
