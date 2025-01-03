import {
  Alert,
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Snackbar,
  TextField,
} from '@mui/material';
import React, { useState } from 'react';
import { Plus } from 'react-feather';
import { HttpError } from '../error/error';
import { createCustomer } from '../service';

export interface AddCustomerProps {
  onCompleted: () => void;
}

export const AddCustomer = ({ onCompleted }: AddCustomerProps) => {
  const [name, setName] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [open, setOpen] = useState(false);
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [error, setError] = useState<string>('');

  const handleError = (error: HttpError) =>
    error.code === 409
      ? setError('Duplicate customer name or email')
      : setError(`${error.code}: ${error.message}`);

  const checkResponse = (response: Response) => {
    if (!response.ok) {
      throw new HttpError(response.status, response.statusText);
    }
  };

  const cleanUp = () => {
    setOpen(false);
    setName('');
    setEmail('');
  };

  const handleSave = () => {
    setShowConfirmation(false);
    setError('');

    createCustomer({ name, email })
      .then((response) => checkResponse(response))
      .then(() => cleanUp())
      .then(() => onCompleted())
      .catch(handleError)
      .finally(() => setShowConfirmation(true));
  };

  const handleName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setName((event.target as HTMLInputElement).value);
  };

  const handleEmail = (event: React.ChangeEvent<HTMLInputElement>) => {
    setEmail((event.target as HTMLInputElement).value);
  };

  const handleCloseAddDialog = () => {
    setOpen(false);
  };

  return (
    <>
      <Button
        variant="contained"
        sx={{ float: 'right' }}
        onClick={() => setOpen(true)}
        startIcon={<Plus />}
      >
        <Box sx={{ display: { xs: 'none', sm: 'block' } }}>Add Customer</Box>
      </Button>

      <Dialog
        open={open}
        onClose={onCompleted}
        aria-labelledby="simple-modal-title"
        aria-describedby="simple-modal-description"
        sx={{ mb: { xs: -3, md: 1 }, mt: { xs: 1 } }}
      >
        <DialogTitle
          sx={{ display: { xs: 'none', md: 'block' } }}
          id="form-dialog-title"
        >
          Add customer {name}
        </DialogTitle>
        <DialogContent sx={{ mb: { xs: -3, md: 1 }, mt: { xs: 0 } }}>
          <Box sx={{ mb: { xs: 0, md: 1 } }}>
            <TextField
              autoFocus
              margin="dense"
              id="name"
              label="Name"
              type="text"
              fullWidth
              InputLabelProps={{
                shrink: true,
              }}
              onChange={handleName}
              value={name}
            />

            <TextField
              margin="dense"
              id="email"
              label="Email"
              type="text"
              fullWidth
              onChange={handleEmail}
              value={email}
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <>
            <Button onClick={handleCloseAddDialog}>Close</Button>
            <Button variant="contained" onClick={handleSave}>
              Save
            </Button>
          </>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={showConfirmation}
        autoHideDuration={4000}
        onClose={() => setShowConfirmation(false)}
      >
        <Alert severity={error ? 'error' : 'success'}>
          {error ? error : 'Done'}
        </Alert>
      </Snackbar>
    </>
  );
};
