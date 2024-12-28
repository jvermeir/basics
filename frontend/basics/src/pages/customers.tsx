import {
  Container,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material';
import { useEffect, useState } from 'react';
import { Customer } from '../components/customer/customer';
import { CustomerData, getCustomers } from '../components/service';
import {Loading} from "../components/loading/loading";
import {AddCustomer} from "../components/customer/add-customer";

export const CustomersPage = () => {
  const [customers, setCustomers] = useState<CustomerData[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<boolean>(false);

  useEffect(() => {
    getCustomers(setCustomers, setLoading, setError);
  }, []);

  function refetch() {
    getCustomers(setCustomers, setLoading, setError);
  }

  return (
    <>
      <Container>
        {!error && loading && <Loading />}
        {error && !loading && (
          <Typography color="textPrimary" mt={3}>
            Error while loading customers.
          </Typography>
        )}
        {!error && !loading && (
          <>
            <Typography color="textPrimary" variant="h2" mt={6} mb={2}>
              Customers
              <AddCustomer onCompleted={refetch} />
            </Typography>
            {(!customers || customers?.length === 0) && (
              <Typography color="textPrimary">No customers, yet.</Typography>
            )}
            {customers && customers.length > 0 && (
              <TableContainer component={Paper}>
                <Table aria-label="simple table">
                  <TableHead>
                    <TableRow>
                      <TableCell>
                        <b>Name</b>
                      </TableCell>
                      <TableCell>
                        <b>Email</b>
                      </TableCell>
                      <TableCell />
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {customers
                      .sort((a, b) => {
                        if (
                          a?.name === b?.name ||
                          !a?.name ||
                          !b?.name
                        )
                          return 0;
                        return a.name < b.name ? -1 : 1;
                      })
                      .filter((customer) => !!customer)
                      .map((customer) => (
                        <Customer
                          key={customer.id}
                          customer={customer}
                          onCompleted={refetch}
                        />))}
                  </TableBody>
                </Table>
              </TableContainer>
            )}
          </>
        )}
      </Container>
    </>
  );
};


export default CustomersPage;
