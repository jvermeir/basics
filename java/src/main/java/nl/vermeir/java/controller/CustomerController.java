package nl.vermeir.java.controller;

import nl.vermeir.java.model.Customer;
import nl.vermeir.java.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path="/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping(path="/all")
    public @ResponseBody Iterable<Customer> getAllUsers() {
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
}
