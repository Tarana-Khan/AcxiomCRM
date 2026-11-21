
package com.acxiom.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acxiom.crm.entity.Customer;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.CustomerStatus;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByOwner(User owner);

    List<Customer> findByStatus(CustomerStatus status);

    List<Customer> findByCustomerNameContainingIgnoreCase(String name);

    List<Customer> findByCompanyNameContainingIgnoreCase(String companyName);

    long countByStatus(CustomerStatus status);

}
