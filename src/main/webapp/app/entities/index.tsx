import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OHDC from './ohdc';
import Requirements from './requirements';
import PackageInclusionsExclusions from './package-inclusions-exclusions';
import FlightDetails from './flight-details';
import Passenger from './passenger';
import PackageTour from './package-tour';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}ohdc`} component={OHDC} />
      <ErrorBoundaryRoute path={`${match.url}requirements`} component={Requirements} />
      <ErrorBoundaryRoute path={`${match.url}package-inclusions-exclusions`} component={PackageInclusionsExclusions} />
      <ErrorBoundaryRoute path={`${match.url}flight-details`} component={FlightDetails} />
      <ErrorBoundaryRoute path={`${match.url}passenger`} component={Passenger} />
      <ErrorBoundaryRoute path={`${match.url}package-tour`} component={PackageTour} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
