
package com.acxiom.crm.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acxiom.crm.entity.FollowUp;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.FollowUpStatus;

public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

    List<FollowUp> findByAssignedTo(User user);

    List<FollowUp> findByStatus(FollowUpStatus status);

    List<FollowUp> findByFollowUpDate(LocalDate date);

    List<FollowUp> findByFollowUpDateBetween(
            LocalDate startDate, LocalDate endDate);

    long countByStatus(FollowUpStatus status);

}
