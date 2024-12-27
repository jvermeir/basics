package nl.vermeir.java.controller;

import nl.vermeir.java.model.Customer;
import nl.vermeir.java.service.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@CrossOrigin
@Controller
@RequestMapping(path="/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping()
    public @ResponseBody Iterable<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public @ResponseBody Optional<Customer> findById(@PathVariable String id) {
        return customerService.findCustomerById(id);
    }

    @PostMapping
    public @ResponseBody Customer addNewCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @PutMapping
    public @ResponseBody Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody void deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
    }
}
