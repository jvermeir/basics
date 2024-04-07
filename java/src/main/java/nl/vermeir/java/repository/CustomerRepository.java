package nl.vermeir.java.repository;

import nl.vermeir.java.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {

}