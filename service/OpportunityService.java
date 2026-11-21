
package com.acxiom.crm.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acxiom.crm.entity.Opportunity;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.OpportunityStage;
import com.acxiom.crm.exception.ResourceNotFoundException;
import com.acxiom.crm.repository.OpportunityRepository;

@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;

    public OpportunityService(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    public List<Opportunity> getAllOpportunities() {
        return opportunityRepository.findAll();
    }

    public Opportunity getOpportunityById(Long id) {
        return opportunityRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Opportunity not found: " + id));
    }

    public List<Opportunity> getOpportunitiesByOwner(User owner) {
        return opportunityRepository.findByOwner(owner);
    }

    public List<Opportunity> getOpportunitiesByStage(OpportunityStage stage) {
        return opportunityRepository.findByStage(stage);
    }

    @Transactional
    public Opportunity saveOpportunity(Opportunity opportunity) {
        return opportunityRepository.save(opportunity);
    }

    @Transactional
    public Opportunity updateOpportunity(
            Long id, Opportunity updatedOpportunity) {

        Opportunity opportunity = getOpportunityById(id);

        opportunity.setOpportunityName(updatedOpportunity.getOpportunityName());
        opportunity.setAmount(updatedOpportunity.getAmount());
        opportunity.setProbability(updatedOpportunity.getProbability());
        opportunity.setStage(updatedOpportunity.getStage());
        opportunity.setExpectedCloseDate(updatedOpportunity.getExpectedCloseDate());

        // Update customer only when a new customer is provided
        if (updatedOpportunity.getCustomer() != null) {
            opportunity.setCustomer(updatedOpportunity.getCustomer());
        }

        // Update owner only when a new owner is provided
        if (updatedOpportunity.getOwner() != null) {
            opportunity.setOwner(updatedOpportunity.getOwner());
        }

        return opportunityRepository.save(opportunity);
    }

    @Transactional
    public Opportunity updateStage(Long id, OpportunityStage stage) {

        Opportunity opportunity = getOpportunityById(id);
        opportunity.setStage(stage);

        return opportunityRepository.save(opportunity);
    }

    @Transactional
    public void deleteOpportunity(Long id) {
        Opportunity opportunity = getOpportunityById(id);
        opportunityRepository.delete(opportunity);
    }

    public long getTotalOpportunities() {
        return opportunityRepository.count();
    }

    public long getOpportunityCountByStage(OpportunityStage stage) {
        return opportunityRepository.countByStage(stage);
    }

    public BigDecimal getTotalOpportunityValue() {
        return opportunityRepository.findAll().stream()
                .map(Opportunity::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
