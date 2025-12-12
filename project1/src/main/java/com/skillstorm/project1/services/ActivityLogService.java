package com.skillstorm.project1.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skillstorm.project1.models.ActivityLog;
import com.skillstorm.project1.repositories.ActivityLogRepository;

/**
 * This service class is responsible for creating and retrieving activity log entries.
 * Track meaningfuls events such as inventory changes or warehouse modifications.
 */
@Service
public class ActivityLogService {

    /**
     * Constructor injection
     */
    private final ActivityLogRepository activityLogRepository;
    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    /**
     * Writes a new activity entry to the log.
     * @param activityType The type of activity (e.g. INVENTORY_ADDED)
     * @param entityType The type of entity affected (e.g. INVENTORY)
     * @param entityId The ID of the entity involved
     * @param description A human-readable description of the event
     */
    public void log(String activityType, String entityType, Integer entityId, String description) {
        ActivityLog log = new ActivityLog();
        log.setActivityType(activityType);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDescription(description);
        log.setCreatedAt(LocalDateTime.now());
        activityLogRepository.save(log);
    }

    /**
     * Retrieves a list of the most recent activity log entries
     * @param limit The maximum number of logs to return
     * @return A list of recent ActivityLog objects (20 max)
     */
    public List<ActivityLog> getRecentActivity(int limit) {
        return activityLogRepository.findTop20ByOrderByCreatedAtDesc();
    }

}
