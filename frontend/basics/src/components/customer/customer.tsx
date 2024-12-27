import { IconButton, TableRow } from '@mui/material';
import { Delete } from 'react-feather';
import { EditCustomer } from './edit-customer';
import StyledTableCell from '../styled-components/StyledTableCell';
import {baseUrl, CustomerData} from '../service';

export const Customer = ({
  customer,
  onCompleted,
}: {
  customer: CustomerData;
  onCompleted: () => void;
}) => {
  const handleDelete = () => {
    fetch(`${baseUrl}/api/customers/${customer.id}`, {
      method: 'DELETE',
    }).then((_) => onCompleted && onCompleted());
  };

  return (
    <TableRow key={customer.id} hover={true}>
      <StyledTableCell>{customer.name}</StyledTableCell>
      <StyledTableCell>{customer.email}</StyledTableCell>
      <StyledTableCell>
        <EditCustomer customer={customer} onCompleted={onCompleted} />
        <IconButton aria-label="delete" onClick={handleDelete}>
          <Delete size="18" />
        </IconButton>
      </StyledTableCell>
    </TableRow>
  );
};
