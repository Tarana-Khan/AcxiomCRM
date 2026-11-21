
package com.acxiom.crm.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acxiom.crm.entity.FollowUp;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.FollowUpStatus;
import com.acxiom.crm.exception.ResourceNotFoundException;
import com.acxiom.crm.repository.FollowUpRepository;

@Service
public class FollowUpService {

    private final FollowUpRepository followUpRepository;

    public FollowUpService(FollowUpRepository followUpRepository) {
        this.followUpRepository = followUpRepository;
    }

    public List<FollowUp> getAllFollowUps() {
        return followUpRepository.findAll();
    }

    public FollowUp getFollowUpById(Long id) {
        return followUpRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Follow-up not found: " + id));
    }

    public List<FollowUp> getFollowUpsByUser(User user) {
        return followUpRepository.findByAssignedTo(user);
    }

    public List<FollowUp> getFollowUpsByStatus(FollowUpStatus status) {
        return followUpRepository.findByStatus(status);
    }

    public List<FollowUp> getFollowUpsForDate(LocalDate date) {
        return followUpRepository.findByFollowUpDate(date);
    }

    public List<FollowUp> getFollowUpsBetween(
            LocalDate startDate, LocalDate endDate) {
        return followUpRepository.findByFollowUpDateBetween(startDate, endDate);
    }

    @Transactional
    public FollowUp saveFollowUp(FollowUp followUp) {
        return followUpRepository.save(followUp);
    }

    @Transactional
    public FollowUp updateFollowUp(Long id, FollowUp updatedFollowUp) {

        FollowUp followUp = getFollowUpById(id);

        followUp.setSubject(updatedFollowUp.getSubject());
        followUp.setDescription(updatedFollowUp.getDescription());
        followUp.setFollowUpDate(updatedFollowUp.getFollowUpDate());
        followUp.setStatus(updatedFollowUp.getStatus());
        followUp.setCustomer(updatedFollowUp.getCustomer());
        followUp.setLead(updatedFollowUp.getLead());
        followUp.setAssignedTo(updatedFollowUp.getAssignedTo());

        return followUpRepository.save(followUp);
    }

    @Transactional
    public FollowUp updateFollowUpStatus(Long id, FollowUpStatus status) {
        FollowUp followUp = getFollowUpById(id);
        followUp.setStatus(status);
        return followUpRepository.save(followUp);
    }

    @Transactional
    public void deleteFollowUp(Long id) {
        FollowUp followUp = getFollowUpById(id);
        followUpRepository.delete(followUp);
    }

    public long getPendingFollowUpCount() {
        return followUpRepository.countByStatus(FollowUpStatus.PENDING);
    }
}
