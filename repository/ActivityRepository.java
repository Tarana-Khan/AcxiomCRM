
package com.acxiom.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acxiom.crm.entity.Activity;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.ActivityType;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByUser(User user);

    List<Activity> findByType(ActivityType type);

    List<Activity> findAllByOrderByActivityDateDesc();

}
