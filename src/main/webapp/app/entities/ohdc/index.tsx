import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OHDC from './ohdc';
import OHDCDetail from './ohdc-detail';
import OHDCUpdate from './ohdc-update';
import OHDCDeleteDialog from './ohdc-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OHDCUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OHDCUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OHDCDetail} />
      <ErrorBoundaryRoute path={match.url} component={OHDC} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OHDCDeleteDialog} />
  </>
);

export default Routes;
