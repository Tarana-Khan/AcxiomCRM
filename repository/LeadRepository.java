
package com.acxiom.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acxiom.crm.entity.Lead;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.LeadStatus;

public interface LeadRepository extends JpaRepository<Lead, Long> {

    List<Lead> findByOwner(User owner);

    List<Lead> findByStatus(LeadStatus status);

    List<Lead> findByLeadNameContainingIgnoreCase(String name);

    long countByStatus(LeadStatus status);

}
