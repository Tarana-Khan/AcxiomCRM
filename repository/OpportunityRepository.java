
package com.acxiom.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acxiom.crm.entity.Opportunity;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.OpportunityStage;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

    List<Opportunity> findByOwner(User owner);

    List<Opportunity> findByStage(OpportunityStage stage);

    List<Opportunity> findByOpportunityNameContainingIgnoreCase(String name);

    long countByStage(OpportunityStage stage);

}
