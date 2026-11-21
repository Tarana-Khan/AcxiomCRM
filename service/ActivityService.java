
package com.acxiom.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acxiom.crm.entity.Activity;
import com.acxiom.crm.entity.User;
import com.acxiom.crm.enums.ActivityType;
import com.acxiom.crm.exception.ResourceNotFoundException;
import com.acxiom.crm.repository.ActivityRepository;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAllByOrderByActivityDateDesc();
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Activity not found: " + id));
    }

    public List<Activity> getActivitiesByUser(User user) {
        return activityRepository.findByUser(user);
    }

    public List<Activity> getActivitiesByType(ActivityType type) {
        return activityRepository.findByType(type);
    }

    @Transactional
    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    @Transactional
    public Activity updateActivity(Long id, Activity updatedActivity) {

        Activity activity = getActivityById(id);

        activity.setSubject(updatedActivity.getSubject());
        activity.setDescription(updatedActivity.getDescription());
        activity.setType(updatedActivity.getType());
        activity.setActivityDate(updatedActivity.getActivityDate());
        activity.setCustomer(updatedActivity.getCustomer());
        activity.setLead(updatedActivity.getLead());
        activity.setUser(updatedActivity.getUser());

        return activityRepository.save(activity);
    }

    @Transactional
    public void deleteActivity(Long id) {
        Activity activity = getActivityById(id);
        activityRepository.delete(activity);
    }
}
