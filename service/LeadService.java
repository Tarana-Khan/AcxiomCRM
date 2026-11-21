
package com.acxiom.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acxiom.crm.entity.Lead;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.LeadStatus;
import com.acxiom.crm.exception.ResourceNotFoundException;
import com.acxiom.crm.repository.LeadRepository;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    public Lead getLeadById(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Lead not found: " + id));
    }

    public List<Lead> getLeadsByOwner(User owner) {
        return leadRepository.findByOwner(owner);
    }

    public List<Lead> getLeadsByStatus(LeadStatus status) {
        return leadRepository.findByStatus(status);
    }

    public List<Lead> searchLeads(String name) {
        return leadRepository.findByLeadNameContainingIgnoreCase(name);
    }

    @Transactional
    public Lead saveLead(Lead lead) {
        return leadRepository.save(lead);
    }

    @Transactional
    public Lead updateLead(Long id, Lead updatedLead) {

        Lead lead = getLeadById(id);

        lead.setLeadName(updatedLead.getLeadName());
        lead.setEmail(updatedLead.getEmail());
        lead.setPhone(updatedLead.getPhone());
        lead.setCompanyName(updatedLead.getCompanyName());
        lead.setSource(updatedLead.getSource());
        lead.setStatus(updatedLead.getStatus());

        // Update owner only when a new owner is provided
        if (updatedLead.getOwner() != null) {
            lead.setOwner(updatedLead.getOwner());
        }

        return leadRepository.save(lead);
    }

    @Transactional
    public Lead updateLeadStatus(Long id, LeadStatus status) {

        Lead lead = getLeadById(id);
        lead.setStatus(status);

        return leadRepository.save(lead);
    }

    @Transactional
    public void deleteLead(Long id) {
        Lead lead = getLeadById(id);
        leadRepository.delete(lead);
    }

    public long getTotalLeads() {
        return leadRepository.count();
    }

    public long getLeadCountByStatus(LeadStatus status) {
        return leadRepository.countByStatus(status);
    }
}
