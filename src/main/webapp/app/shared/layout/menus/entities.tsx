import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/ohdc">
      OHDC
    </MenuItem>
    <MenuItem icon="asterisk" to="/requirements">
      Requirements
    </MenuItem>
    <MenuItem icon="asterisk" to="/package-inclusions-exclusions">
      Package Inclusions Exclusions
    </MenuItem>
    <MenuItem icon="asterisk" to="/flight-details">
      Flight Details
    </MenuItem>
    <MenuItem icon="asterisk" to="/passenger">
      Passenger
    </MenuItem>
    <MenuItem icon="asterisk" to="/package-tour">
      Package Tour
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
