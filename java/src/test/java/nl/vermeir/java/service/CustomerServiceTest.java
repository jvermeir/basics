package nl.vermeir.java.service;

import nl.vermeir.java.model.Customer;
import nl.vermeir.java.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;


    @BeforeEach
    public void setup() {
    }

    @Test
    public void givenCustomerRecordObject_whenSaveCustomerRecord_thenReturnCustomerRecordObject() {
        // given
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setName( "John");
        customer.setEmail("x@y.com");

        given(customerRepository.save(customer)).willReturn(customer);

        // when
        Customer savedCustomer = customerService.addCustomer(customer);

        // then
        assertThat(savedCustomer).isEqualTo(customer);
    }

    @Test
    public void givenCustomerRecordObject_whenListCustomerRecords_thenReturnListOfCustomerRecordObject() {
        // given
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setName( "John");
        customer.setEmail("x@y.com");

        given(customerRepository.findAll()).willReturn(List.of(customer));

        // when
        Iterable<Customer> allCustomerRecords = customerService.getAllCustomers();
        List<Customer> result = new ArrayList<Customer>();
        allCustomerRecords.iterator().forEachRemaining(result::add);

        // then
        assertThat(result).contains(customer);
        assertThat(result.size()).isEqualTo(1);
    }
}