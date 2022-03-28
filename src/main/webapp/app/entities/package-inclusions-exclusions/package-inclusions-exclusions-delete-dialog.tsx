import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './package-inclusions-exclusions.reducer';

export const PackageInclusionsExclusionsDeleteDialog = (props: RouteComponentProps<{ id: string }>) => {
  const [loadModal, setLoadModal] = useState(false);
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
    setLoadModal(true);
  }, []);

  const packageInclusionsExclusionsEntity = useAppSelector(state => state.packageInclusionsExclusions.entity);
  const updateSuccess = useAppSelector(state => state.packageInclusionsExclusions.updateSuccess);

  const handleClose = () => {
    props.history.push('/package-inclusions-exclusions');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(packageInclusionsExclusionsEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="packageInclusionsExclusionsDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="skylifeTravelAndToursApp.packageInclusionsExclusions.delete.question">
        Are you sure you want to delete this PackageInclusionsExclusions?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button
          id="jhi-confirm-delete-packageInclusionsExclusions"
          data-cy="entityConfirmDeleteButton"
          color="danger"
          onClick={confirmDelete}
        >
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default PackageInclusionsExclusionsDeleteDialog;
