import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PackageInclusionsExclusions from './package-inclusions-exclusions';
import PackageInclusionsExclusionsDetail from './package-inclusions-exclusions-detail';
import PackageInclusionsExclusionsUpdate from './package-inclusions-exclusions-update';
import PackageInclusionsExclusionsDeleteDialog from './package-inclusions-exclusions-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PackageInclusionsExclusionsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PackageInclusionsExclusionsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PackageInclusionsExclusionsDetail} />
      <ErrorBoundaryRoute path={match.url} component={PackageInclusionsExclusions} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PackageInclusionsExclusionsDeleteDialog} />
  </>
);

export default Routes;
