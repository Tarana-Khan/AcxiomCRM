
package com.acxiom.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acxiom.crm.entity.Customer;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.CustomerStatus;
import com.acxiom.crm.exception.ResourceNotFoundException;
import com.acxiom.crm.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Customer not found: " + id));
    }

    public List<Customer> getCustomersByOwner(User owner) {
        return customerRepository.findByOwner(owner);
    }

    public List<Customer> getCustomersByStatus(CustomerStatus status) {
        return customerRepository.findByStatus(status);
    }

    public List<Customer> searchCustomers(String name) {
        return customerRepository.findByCustomerNameContainingIgnoreCase(name);
    }

    @Transactional
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer updatedCustomer) {

        Customer customer = getCustomerById(id);

        customer.setCustomerName(updatedCustomer.getCustomerName());
        customer.setEmail(updatedCustomer.getEmail());
        customer.setPhone(updatedCustomer.getPhone());
        customer.setCompanyName(updatedCustomer.getCompanyName());
        customer.setCity(updatedCustomer.getCity());
        customer.setAddress(updatedCustomer.getAddress());
        customer.setStatus(updatedCustomer.getStatus());

        // Update owner only when a new owner is provided
        if (updatedCustomer.getOwner() != null) {
            customer.setOwner(updatedCustomer.getOwner());
        }

        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    public long getTotalCustomers() {
        return customerRepository.count();
    }

    public long getActiveCustomerCount() {
        return customerRepository.countByStatus(CustomerStatus.ACTIVE);
    }
}
